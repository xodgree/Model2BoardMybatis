package controller;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import controller.CommandHandler;
import controller.NullHandler;

public class BoardController  extends HttpServlet {

    //Map 객체를 만듬. (string =>/board/list,CommandHandler => handler.board.ListAction )
	// 명령어와 명령어 처리 클래스를 쌍으로 지정. /board/list를 하면 ListAction으로 감.
    private Map<String, CommandHandler> commandHandlerMap = new HashMap<>();
    
    //init() 메서드 = 객체 생성 후 초기화 작업. (최초 요청시만 서블릿 객체 생성하고, 이후 요청은 앞에 만든 객체를 사용한다.)
    //서블릿을 초기화 할때 사용.
    public void init() throws ServletException {
        //configFile  초기화 파라미터의 값을 읽어온다.
    	String configFile = getInitParameter("configFile");
        Properties prop = new Properties();
        
        //읽어온 값을 이용해서 설정 파일 경로를 구한다.
        String configFilePath = getServletContext().getRealPath(configFile);
        
        /*설정 파일로부터 매핑 정보를 읽어와 Properties 객체에 저장한다. Properties는(이름,값)목록을 갖는 클래스로서,
           	이 경우 에는 프로퍼티  이름을 커맨드 이름으로 사용하고 값을 클래스 이름으로 사용한다.
        */
        try (FileReader fis = new FileReader(configFilePath)) {
            //Properties의 load()메서드를 사용하면 설정 파일로부터 프로퍼티 정보를 읽어올수있다.
        	prop.load(fis);
        } catch (IOException e) {
            throw new ServletException(e);
        }
        
        /*
         * Properties에 저장된 각 프로퍼티의 키에 대해 다음 작업을 반복한다.
         * 1.프로퍼티 이름을 커맨드 이름으로 사용한다.
         * 
         * */
        Iterator keyIter = prop.keySet().iterator();
        while (keyIter.hasNext()) {
        	// 1.프로퍼티 이름을 커맨드 이름으로 사용한다.
            String command = (String) keyIter.next();
            
            // 2.커맨드 이름에 해당하는 핸들러 클래스 이름을 Properties에서 구한다.
            String handlerClassName = prop.getProperty(command);
            
            try {
            	// 3. 핸들러 클래스 이름을 이용해서 Class객체를 구한다.
                Class<?> handlerClass = Class.forName(handlerClassName);
                
                // 4. Class로부터 핸들러 객체를 생성한다. 즉, 2번 과정에서 구한 이름에 해당하는 클래스의 객체를 생성한다.
                CommandHandler handlerInstance = (CommandHandler) handlerClass.newInstance();
                
                // 5. commandHandlerMap에 (커맨드, 핸들러 객체)매핑 정보를 저장한다.
                commandHandlerMap.put(command, handlerInstance);
                
            } catch (ClassNotFoundException | InstantiationException 
            		| IllegalAccessException e) {
                throw new ServletException(e);
            }
        }
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        process(request, response);
    }

    protected void doPost(HttpServletRequest request,
    HttpServletResponse response) throws ServletException, IOException {
        process(request, response);
    }

    private void process(HttpServletRequest request,
    HttpServletResponse response) throws ServletException, IOException {
    	
    	
        String command = request.getRequestURI();
        
        if (command.indexOf(request.getContextPath())==0) {
        	command = command.substring(request.getContextPath().length());
        }
        
        //매핑 프로세스
        //commandHandlerMap에서 요청을 처리할 핸들러 객체를 구한다. cmd 파라미터 값을 키로 사용한다.
        CommandHandler handler = commandHandlerMap.get(command);
        
        //명령어에 해당하는 핸들러 객체가 존재하지 않을 경우 NullHandler를 사용한다. NullHandler클래스는 404에러를 응답으로 전송하는 핸들러 클래스다.
        if(handler ==null) {
        	handler = new NullHandler();
        }
        String viewPage = null;
        try {
        	// 그 주소의 그 process 진행하라. 진행하고 viewPage 리턴
        	/*
        	 * 구한 핸들러 객체의 process()메서드를 호출해서 요청을 처리하고, 결과로 보여줄 뷰 페이지 경로를 리턴 값으로 전달받는다.
        	 * 핸들러 인스턴스인 handler의  process()메소드는 클라이언트의 요청을 알맞게 처리한 후, 뷰 페이지에 보여줄 결과값을 
        	 * request나 session의 속성에 저장해야 한다.
        	 * */
            viewPage = handler.process(request, response);
            
        } catch (Throwable e) {
            throw new ServletException(e);
        }
        // viewPage가 null이 아닐 경우, 핸들러 인스턴스가 리턴한 뷰 페이지로 이동한다.
        if (viewPage != null) {
	        RequestDispatcher dispatcher = request.getRequestDispatcher(viewPage);
	        dispatcher.forward(request, response);
        }
    }
}
