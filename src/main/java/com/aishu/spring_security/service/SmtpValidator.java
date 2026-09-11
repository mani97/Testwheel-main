//package com.aishu.spring_security.service;
//
//import org.apache.commons.net.smtp.SMTPClient;
//import org.apache.commons.net.smtp.SMTPReply;
//import org.xbill.DNS.Lookup;
//import org.xbill.DNS.MXRecord;
//import org.xbill.DNS.Type;
//import org.xbill.DNS.Record;   // dnsjava Record
//
//
//
//public class SmtpValidator {
//    public static void main(String[] args) {
//        String email = "test@gmail.com";
//        try {
//            String email1 = "test@gmail.com";
//            String domain = email.substring(email.indexOf("@") + 1);
//
//            // Step 1: MX lookup using dnsjava
//            Record[] records = new Lookup(domain, Type.MX).run();
//            if (records == null) {
//                System.out.println("No MX records found for domain: " + domain);
//                return;
//            }
//
//            MXRecord mx = (MXRecord) records[0];
//            String mxHost = mx.getTarget().toString();
//            System.out.println("Found MX: " + mxHost);
//
//            // Step 2: SMTP handshake using Apache Commons Net
//            SMTPClient client = new SMTPClient();
//            client.connect(mxHost);
//
//            int reply = client.getReplyCode();
//            if (!SMTPReply.isPositiveCompletion(reply)) {
//                client.disconnect();
//                System.out.println("SMTP server refused connection.");
//                return;
//            }
//
//            client.login("localhost"); // HELO/EHLO
//            client.setSender("validator@yourdomain.com"); // MAIL FROM
//            client.addRecipient(email); // RCPT TO
//
//            reply = client.getReplyCode();
//            System.out.println("Server reply: " + reply);
//
//            client.logout();
//            client.disconnect();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}
