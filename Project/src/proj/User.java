package proj;

public class User {
	private String name;
	private String type;
	
	public final static String MANAGER = "MANAGER";
	public final static String CLERK = "CLERK";
	
	User (String name, String type){
		this.name = name;
		this.type = type;
	}
	
	User (String name){
		this.name = name;
		this.type = CLERK;
	}
	
	
	
	

}
