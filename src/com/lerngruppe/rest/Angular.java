package com.lerngruppe.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/angular")
public class Angular {

	@POST
	@Path("restart")
	@Produces(MediaType.TEXT_PLAIN)
	public Response restartAnglular() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				System.out.println(runCommand("taskkill /F /IM node.exe"));
				System.out.println(runCommand("taskkill /F /IM conhost.exe"));
				System.out.println(runCommand("G:\\testProjects\\angular-crud-level-one\\gitPull.bat"));
				System.out.println(runCommand("G:\\testProjects\\angular-crud-level-one\\restart.bat"));
			}
			
		}).start();	
		
		return Response.status(200).entity("Cheers bud!").build();
	}
	private String runCommand(String command) {
		InputStream is;
		try {
			is = Runtime.getRuntime().exec(command).getInputStream();
			String result = new BufferedReader(new InputStreamReader(is))
					.lines().collect(Collectors.joining("\n"));
			return result;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
