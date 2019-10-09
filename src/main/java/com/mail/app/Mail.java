package com.mail.app;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

@WebServlet("/Mail")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 3, maxFileSize = 1024 * 1024 * 6, maxRequestSize = 1024 * 1024 * 20)
public class Mail extends HttpServlet {

    private static final long serialVersionUID = 1L;

    InputStream is = null;

    public Mail() {

	// TODO Auto-generated constructor stub
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {

	// TODO Auto-generated method stub
	doGet(request, response);
	// response.setContentType("text/html");
	response.setContentType("application/vnd.ms-excel");
	response.setHeader("Content-Disposition", "attachment; filename=ReportsData.xls");
	PrintWriter out = response.getWriter();

	// String to = request.getParameter("to");
	try {
	    Collection<Part> parts = request.getParts();
	    String path = "";
	    /*
	     * for (Part part : parts) { path = printPart(part); if (!path.equals("")) {
	     * break; } }
	     */
	    Object arr[] = parts.toArray();
	    Part excel = (Part) arr[0];
	    path = printPart(excel);
	    is = excel.getInputStream();
	    String subject = request.getParameter("sub");
	    String msg = request.getParameter("msg");
	    String from = request.getParameter("from");
	    String pword = request.getParameter("pword");
	    // String path = request.getParameter("file");

	    if (subject == "" || msg == "" || from == "" || pword == "" || path == "") {
		RequestDispatcher rd = request.getRequestDispatcher("index.html");
		rd.include(request, response);

		out.println("please fill all details, every detail is must and should :)");
	    } else {

		SendEmail se = new SendEmail();
		se.setMailServerProperties();
		Excel ex = new Excel(path, is);

		List<String> resultList = new ArrayList<String>();
		Map<String, String> resultMap = new HashMap<String, String>();
		List l;

		l = ex.getEmailList(path);
		// se.createEmailMessage(l.toArray(), subject, msg);
		// se.sendEmail(from, pword);
		out.println("<table border=1 width=50% height=50%>");
		out.println("<tr><th>Email</th><th>Status</th><tr>");
		for (int i = 0; i < l.size(); i++) {
		    se.createEmailMessage(l.get(i).toString(), subject, msg);
		    int t = se.sendEmail(from, pword);
		    if (t == 1) {
			out.println("<tr><td>" + l.get(i).toString() + "</td><td>" + "success" + "</td></tr>");
			// out.println(l.get(i).toString() + " = success<br>");
			resultList.add("success");
			resultMap.put(l.get(i).toString(), "success");
		    } else if (t == 22) {
			out.println("<tr><td>" + l.get(i).toString() + "</td><td>" + "failed due to no email address"
				+ "</td></tr>");
			// out.println(l.get(i).toString() + " = failed due to no email address<br>");
			resultList.add("failed");
			resultMap.put(l.get(i).toString(), "failed due to no email address");
		    } else if (t == 2) {

			out.println("<tr><td>" + l.get(i).toString() + "</td><td>" + "failed due to invalid address"
				+ "</td></tr>");
			// out.println(l.get(i).toString() + " = failed due to invalid address<br>");
			resultList.add("failed");
			resultMap.put(l.get(i).toString(), "failed due to invalid address");
		    } else if (t == 3) {
			out.println("<tr><td>" + l.get(i).toString() + "</td><td>"
				+ "failed due Authentication fail to your account" + "</td></tr>");
			// out.println(l.get(i).toString() + " = failed due Authentication fail to your
			// account<br>");
			resultList.add("failed");
			resultMap.put(l.get(i).toString(), "failed");
		    } else {
			out.println("<tr><td>" + l.get(i).toString() + "</td><td>" + "failed" + "</td></tr>");
			// out.println(l.get(i).toString() + " = failed<br>");
			resultList.add("failed");
			resultMap.put(l.get(i).toString(), "failed");
		    }
		}

		out.println("</table>");
		out.println("</html></body>");
		// System.out.println(resultList);

		// ex.setList(resultList);
	    }
	} catch (FileNotFoundException e) {
	    RequestDispatcher rd = request.getRequestDispatcher("index.html");
	    rd.include(request, response);

	    out.println("please select the file");
	} catch (Exception e) {
	    e.printStackTrace();
	}

    }

    private String extractFileName(Part part) {
	String contentDisp = part.getHeader("content-disposition");
	String[] items = contentDisp.split(";");
	for (String s : items) {
	    // System.out.println(s);
	    if (s.trim().startsWith("filename")) {
		return s.substring(s.lastIndexOf(File.separator) + 1, s.length() - 1);
	    }
	}
	return "fail";
    }

    private String printPart(Part part) {
	String name = "";
	for (String header : part.getHeaderNames()) {
	    String s = part.getHeader(header);
	    String str[] = s.split(";");

	    for (int i = 0; i < str.length; i++) {
		if (str[i].contains("filename")) {
		    name = str[i].trim();
		    name = str[i].substring(11, str[i].length() - 1);
		    name = name.replace("\\", "\\\\");

		    // System.out.println(name);
		    break;
		}
	    }
	    if (!name.equals("")) {
		break;
	    }

	}
	return name;
    }

}
