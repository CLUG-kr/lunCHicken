package test;

import org.json.simple.JSONObject;

public interface TestJson {
	
	public static void main(String[] args) {
		JSONObject mother = new JSONObject();
		JSONObject son = new JSONObject();
		mother.put("iam", "mother");
		son.put("iam", "son");
		mother.put("item", son);
		System.out.println(mother.toJSONString());
	}
	
}
