package org.wiztools.amazonsescli;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Properties;
import joptsimple.OptionException;
import joptsimple.OptionParser;
import joptsimple.OptionSet;

/**
 *
 * @author subWiz
 */
public class AmazonSESMain {
    private static void printHelp(PrintStream out) {
        out.println("Usage: ");
        out.println("\t-k /path/to/aws-credentials.properties -s \"Mail subject\" -f \"sender@domain.tld\" \\");
        out.println("\t\tto_1@domain.tld to_2@domain.tld ... < message.txt");
        out.println();
        out.println("Format of `aws-credentials.properties':");
        out.println("\tAWSAccessKeyId=XXX");
        out.println("\tAWSSecretKey=XXX");
    }
    
    private static String readFromSTDIN() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String s = null;
        StringBuilder sb = new StringBuilder();
        while((s=br.readLine())!=null) {
            sb.append(s);
            sb.append("\n");
        }
        return sb.toString();
    }
    
    public static void main(String[] args) {
        // Parse the command-line:
        OptionParser parser = new OptionParser("hk:s:f:");
        try {
            OptionSet options = parser.parse(args);

            if(options.has("h")) { // display help
                printHelp(System.out);
                System.exit(0);
            }

            if(!(options.has("k") && options.has("s") && options.has("f"))) {
                System.err.print("One required argument missing. Usage: ");
                printHelp(System.err);
                System.exit(1);
            }
            
            if(options.nonOptionArguments().isEmpty()) {
                System.err.println("There should be atleast one to-email-address. Usage: ");
                printHelp(System.err);
                System.exit(1);
            }
            
            // Create the MailBean:
            final MailBean bean = new MailBean();
            bean.setFrom(options.valueOf("f").toString());
            for(String to: options.nonOptionArguments()) {
                bean.addTo(to);
            }
            
            // Read message body from STDIN:
            try{
                final String message = readFromSTDIN();
                bean.setMessage(message);
            }
            catch(IOException ex) {
                System.err.println(ex.getMessage());
                System.exit(2);
            }
            
            // Load the credentials:
            Properties p = new Properties();
            try{
                p.load(new FileInputStream(new File(options.valueOf("k").toString())));
            }
            catch(IOException ex) {
                System.err.println(ex.getMessage());
                System.exit(3);
            }
            
            Credentials creds = new Credentials();
            creds.setAwsAccessKeyId(p.getProperty("AWSAccessKeyId"));
            creds.setAwsSecretKey(p.getProperty("AWSSecretKey"));
            
            // Now send the mail:
            AWSSeSExecuter.execute(creds, bean);
        }
        catch(OptionException ex) {
            System.err.println(ex.getMessage());
            printHelp(System.err);
            System.exit(1);
        }
    }
}
