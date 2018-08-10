package com.nv.test.jms.sender;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;

import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.nv.test.utils.FileUtils;

/**
 * @author naveenvemulapalli
 *
 */
public class QueueSend {
	
	private QueueConnectionFactory qconFactory;
	private QueueConnection qcon;
	private QueueSession qsession;
	private QueueSender qsender;
	private Queue queue;
	private TextMessage msg;

	public void init(Context ctx, String jmsFactory, String queueName) throws NamingException,
			JMSException {
		qconFactory = (QueueConnectionFactory) ctx.lookup(jmsFactory);
		qcon = qconFactory.createQueueConnection();
		qsession = qcon.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
		queue = (Queue) ctx.lookup(queueName);
		qsender = qsession.createSender(queue);
		msg = qsession.createTextMessage();
		qcon.start();
	}

	public void init(Context ctx, String jmsFactory, String queueName, String correlationID)
			throws NamingException, JMSException {
		qconFactory = (QueueConnectionFactory) ctx.lookup(jmsFactory);
		qcon = qconFactory.createQueueConnection();
		qsession = qcon.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
		queue = (Queue) ctx.lookup(queueName);
		qsender = qsession.createSender(queue);
		msg = qsession.createTextMessage();
		msg.setJMSCorrelationID(correlationID);
		qcon.start();
	}

	public void send(String message) throws JMSException {
		msg.setText(message);
		qsender.send(msg);
	}

	public void close() throws JMSException {
		qsender.close();
		qsession.close();
		qcon.close();
	}

	public static void sendToQueue(String fileLocation, String url, String jndiFactory, String jmsFactory, String jmsQueue, String correlationId) throws NamingException, JMSException, IOException {

		InitialContext ic = getInitialContext(url, jndiFactory);
		QueueSend qs = new QueueSend();
		if(correlationId !="")
			qs.init(ic, jmsFactory, jmsQueue, correlationId);
		else
			qs.init(ic, jmsFactory, jmsQueue);
		
		File file = new File(fileLocation);
		String msg = FileUtils.fileToString(file);
		readAndSend(qs, msg);
		qs.close();
	}

	private static void readAndSend(QueueSend qs, String msg) throws IOException,
			JMSException {
				qs.send(msg);
	}

	private static InitialContext getInitialContext(String url, String jndiFactory)
			throws NamingException {
		Hashtable env = new Hashtable();
		env.put(Context.INITIAL_CONTEXT_FACTORY, jndiFactory);
		env.put(Context.PROVIDER_URL, url);
		return new InitialContext(env);
	}
	
	
}

