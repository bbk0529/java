package net.bkpark.chat.dev;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ibm.watson.developer_cloud.conversation.v1.Conversation;
import com.ibm.watson.developer_cloud.conversation.v1.model.Context;
import com.ibm.watson.developer_cloud.conversation.v1.model.InputData;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageOptions;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageResponse;

@RestController
public class WatsonSay {
	
	@Value("${conversation.username}")
	private  String username;
	
	@Value("${conversation.password}")
	private  String password;
	
	@Value("${conversation.workspaceid}")
	private  String workspaceid;
	
	private static Logger logger = LoggerFactory.getLogger(WatsonSay.class);
	
	//static Context context;//
	
	@RequestMapping(value="watsonsay")
	public MessageResponse watsonsay(String isay, HttpSession session) {
		logger.info("user input" + isay);
	
		Conversation service = new Conversation(Conversation.VERSION_DATE_2017_05_26);
		service.setUsernameAndPassword(username, password);

		MessageResponse response = null;
		Context context = (Context) session.getAttribute("context");
		MessageOptions options = null;
		String msg= isay;
		StringBuffer watsonSay = null;
		options = new MessageOptions.Builder()
			    .workspaceId(workspaceid)
			    .input(new InputData.Builder(msg).build())
			    .context(context)
			    .build();
		
		response=service.message(options).execute();
		logger.info("Watson : ");
		 
		/*watsonSay = new StringBuffer();
		for (String text : response.getOutput().getText()) {
			watsonSay.append(text);
			watsonSay.append(" ");
		}*/
		logger.info("Watson : " + watsonSay);
		context=response.getContext();
		session.setAttribute("context", context);
		return response;
	}
}
