package com.bharath.jms.claimmanagement;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.*;
import javax.naming.InitialContext;

public class ClaimManagement {

    public static void main(String[] args) throws Exception {
        InitialContext initialContext = new InitialContext();
        Queue requestQueue = (Queue) initialContext.lookup("queue/claimQueue");

        try(ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory(); JMSContext jmsContext = cf.createContext()) {

            JMSProducer producer = jmsContext.createProducer();
//            JMSConsumer consumer = jmsContext.createConsumer(requestQueue, "hospitalId=1");
//            JMSConsumer consumer = jmsContext.createConsumer(requestQueue, "claimAmount BETWEEN 1000 AND 5000");
//            JMSConsumer consumer = jmsContext.createConsumer(requestQueue, "doctorName LIKE 'Joh_'");
            JMSConsumer consumer = jmsContext.createConsumer(requestQueue, "doctorType IN ('neuro', 'psych') OR JMSPriority BETWEEN 3 AND 6");

            ObjectMessage objectMessage = jmsContext.createObjectMessage();
//            objectMessage.setIntProperty("hospitalId", 1);
//            objectMessage.setDoubleProperty("claimAmount", 1000);
//            objectMessage.setStringProperty("doctorName", "John");
            objectMessage.setStringProperty("doctorType", "gyna");
            Claim claim = new Claim();
            claim.setHospitalId(1);
            claim.setClaimAmount(1000);
            claim.setDoctorName("John");
            claim.setDoctorType("gyna");
            claim.setInsuranceProvider("blue cross");
            objectMessage.setObject(claim);

            producer.send(requestQueue, objectMessage);

            Claim receiveBody = consumer.receiveBody(Claim.class);
            System.out.println(receiveBody.getClaimAmount());
        }
    }

}
