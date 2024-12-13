
class IndexTemplate {

	public String generate(/*TODO: Support template args*/) {
		StringBuilder sb = new StringBuilder();
		sb.append("<DOCTYPE html>");
		sb.append("<html>");
		sb.append("	<head>");
		sb.append("<title>Hello, world");
		sb.append(2 * 2 % 2);
		sb.append(" </title>");
		sb.append("	</head>");
		sb.append("	<body>");
		for (int i = 0; i < 10; i++) {
			sb.append("<h1>");
			sb.append("Hello " + i);
			sb.append(" </h1>");
		}
		sb.append("	</body>");
		sb.append("</html>");
		sb.append("");

		return sb.toString();
	}
}
