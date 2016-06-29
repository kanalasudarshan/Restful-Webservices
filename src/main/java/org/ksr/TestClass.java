package org.ksr;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataBodyPart;
import com.sun.jersey.multipart.FormDataMultiPart;
import com.sun.jersey.multipart.FormDataParam;


@Path(value="/test")
public class TestClass {
	
	@GET
	@Path(value="/{name}")
	public Response getRestData(@PathParam("name") String msg){
		return Response.status(200).entity("Your name is "+msg).build();
	}
	
	@GET
	@Path(value="/getjson")
	@Produces(MediaType.APPLICATION_JSON)
	public TestDTO getJson(){
		TestDTO dto=new TestDTO();
		dto.setId(11);
		dto.setName("sudars reddy");
		return dto;
	}
	
	@GET
	@Path(value="/getxml")
	@Produces(MediaType.APPLICATION_XML)
	public TestDTO getXml(){
		TestDTO dto=new TestDTO();
		dto.setId(11);
		dto.setName("sudars reddy");
		return dto;
	}
	
	@GET
	@Path(value="/getxmllist")
	@Produces(MediaType.APPLICATION_XML)
	public List<TestDTO> getXmlList(){
		List<TestDTO> list=new ArrayList<TestDTO>();
		for(int i=10;i<25;i++){
			TestDTO dto=new TestDTO();
			dto.setId(i);
			dto.setName("sudars reddy");
			list.add(dto);
		}
		return list;
	}
	
		
	
	@GET
	@Path(value="/getjsonmaplist")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Map<Integer,String>> getJsonMapList(){
		List<Map<Integer,String>> list=new ArrayList<Map<Integer,String>>();
		for(int i=10;i<25;i++){
			Map<Integer,String> map=new HashMap<Integer, String>();
			map.put(i,"sudars reddy");			
			list.add(map);
		}
		return list;
	}
	
	@POST
	@Path(value="/uploadfile")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response fileUpload(@FormDataParam("file") InputStream is,@FormDataParam("file") FormDataContentDisposition header){		
		try{
			int read=0;
			FileOutputStream fos=new FileOutputStream("D:/"+header.getFileName());
			while ((read = is.read()) != -1) {  
				fos.write(read);  
			}  
			fos.flush();
			fos.close();
			return Response.status(200).entity("File uploded successfully").build();
		}catch(Exception exception){
			return Response.status(201).entity("File uploded failed").build();
		}
	}

	
	@POST
	@Path(value="/uploadfiles")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response filesUpload(FormDataMultiPart formParams){		
		try{
			Map<String,List<FormDataBodyPart>> multipart=formParams.getFields();
			for(Map.Entry<String,List<FormDataBodyPart>> map:multipart.entrySet()){
				if(map.getKey().equals("file")){
					List<FormDataBodyPart> files=map.getValue();
					for(FormDataBodyPart file:files){		
						storeFile(file.getValueAs(InputStream.class),file.getFormDataContentDisposition().getFileName());
					}
				}				
			}
			
			return Response.status(200).entity("File uploded successfully").build();
		}catch(Exception exception){
			return Response.status(201).entity("File uploded failed").build();
		}
	}
	
	private void storeFile(InputStream is,String filename)throws Exception{
		try{
			int read=0;
			FileOutputStream fos=new FileOutputStream("D:/"+filename);
			while ((read = is.read()) != -1) {  
				fos.write(read);  
			}  
			fos.flush();
			fos.close();
			
		}catch(Exception exception){
			throw new Exception(exception);
		}
	}
	
	@GET
	@Path(value="/downloadfile")
	@Produces(MediaType.MULTIPART_FORM_DATA)
	public Response downloadFile(){	
		ResponseBuilder response=null;
		try{
			File file = new File("D:/ksrapp.rar");
			response = Response.ok((Object) file);	
			response.header("Content-Disposition","attachment; filename=\""+file.getName());
		}catch(Exception exception){
			exception.printStackTrace();
		}
		return response.build();
	}

}
