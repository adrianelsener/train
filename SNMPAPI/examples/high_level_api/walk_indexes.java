/*$Id: walk_indexes.src,v 1.3.2.4 2009/01/28 12:45:56 prathika Exp $*/
/*
 * @(#)walk_indexes.java
 * Copyright (c) 1996-2009 AdventNet, Inc. All Rights Reserved.
 * Please read the COPYRIGHTS file for more details.
 */

/** 
 *  Get SNMP data based on command line arguments.  Loads MIBs 
 *  as specified, and converts to/from names for loaded MIB data. 
 *
 * [-d]                - Debug output. By default off.
 * [-c] <community>    - community String. By default "public".
 * [-p] <port>         - remote port no. By default 161.
 * [-t] <Timeout>      - Timeout. By default 5000ms.
 * [-r] <Retries>      - Retries. By default 0.      
 * [-v] <version>      - version(v1 / v2 / v3). By default v1.
 * [-u] <username>     - The v3 principal/userName
 * [-a] <autProtocol>  - The authProtocol(MD5/SHA). Mandatory if authPassword is specified
 * [-w] <authPassword> - The authentication password.
 * [-s] <privPassword> - The privacy protocol password. Must be accompanied with auth password and authProtocol fields.
 * [-n] <contextName>  - The snmpv3 contextName to be used.
 * [-i] <contextID>    - The snmpv3 contextID to be used.
 * [-pp] <privProtocol> - The privacy protocol. Must be accompanied with auth,priv password and authProtocol fields.
 * host Mandatory      - The RemoteHost (agent).Format (string without double qoutes/IpAddress).
 * OID  Mandatory      - Give multiple no. of Object Identifiers.
 * mib  Mandatory      - The mibs to be loaded.  
 */

import com.adventnet.snmp.beans.*;
import com.adventnet.snmp.snmp2.*;
import com.adventnet.snmp.mibs.*;
import java.util.*;

public class walk_indexes {

    private static final int COMMUNITY = 1;
    private static final int PORT = 2;
    private static final int RETRIES = 3;
    private static final int TIMEOUT = 4;



    private static final int VERSION = 0;
    private static final int USER_NAME = 5;
    private static final int AUTH_PROTOCOL = 6;
    private static final int AUTH_PASSWORD = 7;
    private static final int PRIV_PASSWORD = 8;
	private static final int CONTEXT_NAME = 9;
	private static final int CONTEXT_ID = 10;
    private static final int DEBUG = 11;
    private static final int PRIV_PROTOCOL = 12;

    public static void main(String args[]) {

    // Take care of getting options

    String usage = "walk_indexes [-v version(v1,v2,v3)] [-c community] [-p port] [-t timeout] [-r retries] [-u user] [-a auth_protocol(MD5/SHA)] [-w auth_password] [-s priv_password] [-n contextName] [-i contextID] [-d debug] [ -pp priv_protocol (DES/AES-128/AES-192/AES-256/3DES) ] host OID MIB_File";
    String options[] = { "-v", "-c", "-p", "-r", "-t", "-u", "-a", "-w", "-s", "-n", "-i", "-d", "-pp" };
    String values[] = { null, null, null, null, null, null, null, null, null, null, null, "None", null};

    String authProtocol = new String("NO_AUTH");

    ParseOptions opt = new ParseOptions(args,options,values, usage);
 if(values[VERSION] != null)
  {       
  if ((values[VERSION].equals("v3")) && (values[USER_NAME] == null)) opt.usage_error();
}

    // check for hostname, index, and one OID in remaining arguments
    if (opt.remArgs.length<3) opt.usage_error();

    // Use an SNMP target bean to perform SNMP operations
    SnmpTarget target = new SnmpTarget();
	 //To load MIBs from compiled file
	 target.setLoadFromCompiledMibs(true);
    if (values[DEBUG].equals("Set")) target.setDebug(true);


        if(values[VERSION] != null) {  // if SNMP version is specified, set it
            if(values[VERSION].equals("v2"))
                target.setSnmpVersion( SnmpTarget.VERSION2C ) ;
            else if(values[VERSION].equals("v1"))
                target.setSnmpVersion( SnmpTarget.VERSION1 );
            else if(values[VERSION].equals("v3"))
                target.setSnmpVersion( SnmpTarget.VERSION3 );
            else {
                System.out.println("Invalid Version Number"); 
                System.exit(1);
            }
        }

    target.setTargetHost( opt.remArgs[0] );  // set the agent hostname
    if (values[COMMUNITY] != null) // set the community if specified
        target.setCommunity( values[COMMUNITY] );

    try { // set the timeout/retries/port parameters, if specified
        if (values[PORT] != null) 
            target.setTargetPort( Integer.parseInt(values[PORT]) );
        if (values[RETRIES] != null) 
            target.setRetries( Integer.parseInt(values[RETRIES]) );
        if (values[TIMEOUT] != null) 
            target.setTimeout( Integer.parseInt(values[TIMEOUT]) );
    } catch (NumberFormatException ex) {
        System.err.println("Invalid Integer Argument "+ex);
    }

    if(target.getSnmpVersion() == target.VERSION3) {
        if (values[USER_NAME] != null) 
            target.setPrincipal(values[USER_NAME]);
        if (values[AUTH_PROTOCOL] != null) {
            //System.out.println("authProtocol = " + authProtocol);
            authProtocol = values[AUTH_PROTOCOL];
        }
        if(authProtocol.equals("SHA"))
            target.setAuthProtocol(target.SHA_AUTH);
        else if(authProtocol.equals("MD5"))
            target.setAuthProtocol(target.MD5_AUTH);
        else
            target.setAuthProtocol(target.NO_AUTH);
        if (values[AUTH_PASSWORD] != null) 
            target.setAuthPassword(values[AUTH_PASSWORD]);
        if (values[PRIV_PASSWORD] != null) 
        {
            target.setPrivPassword(values[PRIV_PASSWORD]);
            if(values[PRIV_PROTOCOL] != null)
            {
                    if(values[PRIV_PROTOCOL].equals("AES-128"))
                    {
                       target.setPrivProtocol(target.CFB_AES_128);
                    }
		    else if(values[PRIV_PROTOCOL].equals("AES-192"))
                    {
                       target.setPrivProtocol(target.CFB_AES_192);
                    }
		    else if(values[PRIV_PROTOCOL].equals("AES-256"))
                    {
                       target.setPrivProtocol(target.CFB_AES_256);
                    }
		    else if(values[PRIV_PROTOCOL].equals("3DES"))
                    {
                      target.setPrivProtocol(target.CBC_3DES);
                    }
                    else if(values[PRIV_PROTOCOL].equals("DES"))
                    {
                       target.setPrivProtocol(target.CBC_DES);
                    }
                    else
                    {
                     System.out.println(" Invalid PrivProtocol "+values[PRIV_PROTOCOL]);
                     opt.usage_error();
                    }
            }
            else
            {
               System.out.println(" Specify PrivProtocol ");
               opt.usage_error();
            }
        }

		if(values[CONTEXT_NAME]!= null)
			target.setContextName(values[CONTEXT_NAME]);
		if(values[CONTEXT_ID]!= null)
			target.setContextID(values[CONTEXT_ID]);
    }

    if (opt.remArgs[2] != null) try { // Load the MIB files 
        System.err.println("Loading MIBs: "+opt.remArgs[2]);
        target.loadMibs(opt.remArgs[2]);
    } catch (Exception ex) {
        System.err.println("Error loading MIBs: "+ex);
    }

	if(target.getSnmpVersion() == target.VERSION3){
		target.create_v3_tables();
	}

    try { 
      // create OID array from command line arguments
      String oid = opt.remArgs[1];
      MibOperations mibOps = target.getMibOperations();
      SnmpOID rootoid = mibOps.getSnmpOID(oid);
      MibNode node = mibOps.getMibNode(rootoid);
      if ((node == null) || (node.getSyntax()==null)) {
         System.err.println("Need the MIB and leaf node for this. \n"+oid);
         System.exit(1);
      }

      if(!node.isTableColumn()){
          System.err.println("Need the MIB and leaf node of a table for this. \n"+oid);
          System.exit(1);
      }
      // Set the OID List on the SnmpTarget instance
      target.setSnmpOID(rootoid);

      int maxtry = 0;
      while ( maxtry++ < 1000) {  // limit the max getnexts to 1000
        SnmpVarBind vb = target.snmpGetNextVariableBinding();
        if (vb == null) break;
        if ( !SnmpTarget.isInSubTree(rootoid,target.getSnmpOIDList()[0]) )
          break;  // check first column

        if (maxtry == 1) System.out.println("Response received walking: "+
                        mibOps.toString(rootoid));

        SnmpOID roid = vb.getObjectID();
        String inst = mibOps.getInstanceString(roid);
        Vector indexNodes = node.getIndexes(mibOps);
        Vector v = 
          node.getSyntax().decodeInstanceString(inst, indexNodes);

        StringBuffer sb = new StringBuffer();
        for (int i=0;i<v.size();i++) {
          MibNode indexNode = (MibNode)indexNodes.elementAt(i);
          SnmpVar var = (SnmpVar)v.elementAt(i);
          String s = mibOps.toString( var, mibOps
                      .getSnmpOID(indexNode.getLabel()) );
          if (var instanceof SnmpString) {
        if (indexNode.getLabel().indexOf("Address") != -1)
          s = (new SnmpIpAddress(var.toBytes())).toString();
          }  
          sb.append(" Index"+i+": "+s);
        }
        System.out.println("Indexes: "+sb+"\nValue: "+vb.getVariable());
      }

      if (maxtry == 1) {  // we did not get a valid row
        System.err.println("Request failed, timed out or no available data. \n"+target.getErrorString());
      } 

    } catch (Exception ex) {
      System.err.println("Exception. "+ex);
    }
    System.exit(0);
    

    }

}
