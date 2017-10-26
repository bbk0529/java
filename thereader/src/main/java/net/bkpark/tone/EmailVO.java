package net.bkpark.tone;

import java.io.Serializable;
import java.sql.Date;

public class EmailVO implements Serializable {
	int no;
	public int getNo() {
		return no;
	}
	public void setNo(int no) {
		this.no = no;
	}
	String sender,subject,text,	remark;
	Date date;
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	@Override
	public String toString() {
		return "EmailVO [sender=" + sender + ", subject=" + subject + ", text=" + text + ", remark=" + remark
				+ ", date=" + date + "]";
	}
	
		
	
}