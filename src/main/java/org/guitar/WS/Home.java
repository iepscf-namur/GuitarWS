package org.guitar.WS;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet implementation class Home */
@WebServlet("/Home")
public class Home extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/** Default constructor */
	public Home() {
		// TODO Auto-generated constructor stub
	}

	/** @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response) */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String title = "Home";
		int servletInfo = this.getServletContext().getMajorVersion();
		request.setAttribute("title", title);
		request.setAttribute("servletInfo", servletInfo);

		this.getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);

	}

	/** @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response) */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}

}
