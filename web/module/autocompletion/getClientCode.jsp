
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page import="org.openmrs.module.vcttrac.util.VCTTracAutocompleteUtils"%>
<%
	VCTTracAutocompleteUtils db = new VCTTracAutocompleteUtils();

	String query = request.getParameter("q");

	List<String> clientCodes = db.getMatchingClientCode(query);

	Iterator<String> iterator = clientCodes.iterator();
	while (iterator.hasNext()) {
		String clientCode = (String) iterator.next();
		out.println(clientCode);
	}
%>