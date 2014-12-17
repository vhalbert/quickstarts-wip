/*
 * JBoss, Home of Professional Open Source.
 * See the COPYRIGHT.txt file distributed with this work for information
 * regarding copyright ownership.  Some portions may be licensed
 * to Red Hat, Inc. under one or more contributor license agreements.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301 USA.
 */

import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.FileFilter;

import static org.hamcrest.core.Is.is;

import org.modeshape.web.jcr.rest.client.IJcrConstants;
import org.modeshape.web.jcr.rest.client.IRestClient;
import org.modeshape.web.jcr.rest.client.Status;
import org.modeshape.web.jcr.rest.client.domain.QueryRow;
import org.modeshape.web.jcr.rest.client.domain.Repository;
import org.modeshape.web.jcr.rest.client.domain.Server;
import org.modeshape.web.jcr.rest.client.domain.Workspace;
import org.modeshape.web.jcr.rest.client.json.JsonRestClient;


@SuppressWarnings("nls")
public class JsonRestClientTest {
	
    private static final String URL_PARM = "--url";
    private static final String REPO_PARM = "--repo";
    private static final String WORKSPACENAME_PARM = "--workspacename";

    private static final String WORKSPACEPATH_PARM = "--workspacepath";
    
//    private static final String FILE_PARM = "--file";
//    private static final String DIR_PARM = "--dir";
    private static final String USERNAME_PARM = "--username";
    private static final String PWD_PARM = "--pwd";
//    private static final String UNPUBLISH = "--unpublish";
    private static final String HELP_PARM = "--help";

	
   // user and password configured in pom
    protected String PSWD = null;
    protected String USER = null;
    protected String URL = "http://localhost:8080/modeshape-rest";

    protected String REPOSITORY_NAME = "dv";
    protected String WORKSPACE_NAME = "default";

    protected String WORKSPACE_PATH = "./files/";
    protected String DDL_FILE_PATH = WORKSPACE_PATH + "create_table.ddl";

//    private static final String WORKSPACE_UNUSUALPATH = "/myproject/My.Test - Folder/";
//    private static final String FILE_UNUSUALPATH = WORKSPACE_UNUSUALPATH + "Test File_.a-().txt";

    private IRestClient restClient;
    private Server server;
    private Repository repository1;
    private Workspace workspace1;

//    private File textfile = null;

	
	public static void main(String[] args) throws Exception {
//		if (args.length < 4) {
//			System.out.println("usage: JDBCClient <host> <port> <vdb> <sql-command>");
//			System.exit(-1);
//		}
		
	       if (args == null || args.length == 0 || args[0].equals(HELP_PARM)) {
	            // CHECKSTYLE IGNORE check FOR NEXT 12 LINES
	            System.out.println("Running the ModeShape Rest Client");
	            System.out.println("	required arguments are:");
	            System.out.println("  	 	" + URL_PARM);
	            System.out.println("  		" + REPO_PARM);
//	            System.out.println("  	 	" + FILE_PARM + " or " + DIR_PARM);
	            System.out.println("	optional arguments are:");
	            System.out.println("  	 	" + WORKSPACENAME_PARM + " (default=default)");
	            System.out.println("  		" + WORKSPACEPATH_PARM + " (default=./files/");
//	            System.out.println(" 		" + USERNAME_PARM + "(default=admin");
//	            System.out.println("  	 	" + PWD_PARM + " (default=admin");
//	            System.out.println("  	 	" + UNPUBLISH + " with no parameter, will remove file(s)");

	            System.exit(0);
	        }
	       
	       JsonRestClientTest test = new JsonRestClientTest();

//	        String server_name = null;
//	        String workspace_name = "default";
//	        String workspace_path = null;
//	        String file_name = null;
//	        String dir_loc = null;
//	        String user = "admin";
//	        String pwd = "admin";
//	        String repo_name = null;

	        boolean publish = true;

	        int pos = 0;
	        for (String arg : args) {
	            arg = arg.trim();
	            if (arg.equals(URL_PARM)) {
	                test.URL = args[pos + 1];
	            } else if (arg.equals(REPO_PARM)) {
	                test.REPOSITORY_NAME = args[pos + 1];
	            } else if (arg.equals(WORKSPACENAME_PARM)) {
	                test.WORKSPACE_NAME = args[pos + 1];
	            } else if (arg.equals(WORKSPACEPATH_PARM)) {
	                test.WORKSPACE_PATH = args[pos + 1];
//	            } else if (arg.equals(FILE_PARM)) {
//	                file_name = args[pos + 1];
//	            } else if (arg.equals(DIR_PARM)) {
//	                dir_loc = args[pos + 1];
	            } else if (arg.equals(USERNAME_PARM)) {
	                test.USER = args[pos + 1];
	            } else if (arg.equals(PWD_PARM)) {
	                test.PSWD = args[pos + 1];
//	            } else if (arg.equals(UNPUBLISH)) {
//	                publish = false;
	            }

	            ++pos;
	        }

	        String errparm = null;
	        if (test.server == null) {
	            errparm = URL_PARM;
	        } else if (test.REPOSITORY_NAME == null) {
	            errparm = REPO_PARM;
//	        } else if (file_name == null && dir_loc == null) {
//	            errparm = "[" + FILE_PARM + " | " + DIR_PARM + "]";
	        } else if (test.USER == null) {
	            errparm = USERNAME_PARM;
	        } else if (test.PSWD == null) {
	            errparm = PWD_PARM;
//	        } else if (test.WORKSPACE_PATH == null) {
//	            errparm = WORKSPACEPATH_PARM;
	        }

		
		System.out.println("Start Publishing to ModeShape");
		
		test.runTest();
	}
	
	private void runTest() throws Exception {
        this.restClient = new JsonRestClient();
        // Create and validate the server ...
        this.server = new Server(URL, USER, PSWD);
        try {
            this.server = this.restClient.validate(server);
        } catch (Throwable t) {
            t.printStackTrace(System.err);
        }
        // Now create the repository and workspace objects
        this.repository1 = new Repository(REPOSITORY_NAME, server);
        this.workspace1 = new Workspace(WORKSPACE_NAME, repository1);
        
        publishWorkspaceFiles();
	}
	
	private void publishWorkspaceFiles() throws Exception {
		
		File[] files =  findAllFilesInDirectory(WORKSPACE_PATH);
		if (files != null && files.length > 0) {
			
			for (int i=0; i< files.length; i++) {
				publishResource(files[i]);
			}
		}
	}
	
   private void publishResource(File f) throws Exception {
        
        System.out.println("Publish: " + f.toString());
        Status status = this.restClient.publish(workspace1, WORKSPACE_PATH, f);

        printExceptionIfStatusIsError(status);

        assertThat(status.getMessage(), status.isOk(), is(true));

        // confirm it exists in repository
        assertThat(((JsonRestClient)this.restClient).pathExists(workspace1, WORKSPACE_PATH, f), is(true));
        
        System.out.println("Published: " + f.toString());


    }
    
        private void printExceptionIfStatusIsError( Status status ) {
        if (status.isError()) {
            System.err.println(status.getMessage());
            status.getException().printStackTrace(System.err);
        }
    }
        
    	/**
         * Returns a <code>File</code> array that will contain all the files that exist in the directory
         * 
         * @return File[] of files in the directory
         */
        private static File[] findAllFilesInDirectory(String dir) {

            // Find all files in the specified directory
            File modelsDirFile = new File(dir);
            FileFilter fileFilter = new FileFilter() {

                public boolean accept(File file) {
                    if (file.isDirectory()) {
                        return false;
                    }

                    String fileName = file.getName();

                    if (fileName == null || fileName.length() == 0) {
                        return false;
                    }

                    return true;

                }
            };

            File[] modelFiles = modelsDirFile.listFiles(fileFilter);

            return modelFiles;

        }

}
