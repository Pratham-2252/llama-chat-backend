package com.prathamesh.app.utility;

import java.util.ArrayList;

public class Email {

	private String to;
	private String subject;
	private String body;
	private String cc;
	private String bcc;
	private ArrayList<String> attachments;

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getCc() {
		return cc;
	}

	public void setCc(String cc) {
		this.cc = cc;
	}

	public String getBcc() {
		return bcc;
	}

	public void setBcc(String bcc) {
		this.bcc = bcc;
	}

	public ArrayList<String> getAttachments() {
		return attachments;
	}

	public void setAttachments(ArrayList<String> attachments) {
		this.attachments = attachments;
	}

}
