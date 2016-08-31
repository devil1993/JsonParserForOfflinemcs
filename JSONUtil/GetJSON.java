package JSONUtil;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.String;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

public class GetJSON {
	public static void main(String args[]) {
		HashMap<String, Integer> quantities = new HashMap<String, Integer>();
		String JSON = "";
		List<String> JSONElements = new ArrayList<String>();
		Runtime r = Runtime.getRuntime();
		Process p;
		String url = "http://localhost:8000/tasks/";
		try {
			p = r.exec("curl " + url);
			p.waitFor();
			BufferedReader b = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line = "";

			while ((line = b.readLine()) != null) {
			  JSON = line;
			}

			b.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//If its a JSON array, remove the start and ending brackets.
		if(JSON.startsWith("[")){
			Stack<Character> charStack = new Stack<Character>();
			JSON = JSON.substring(1, JSON.length()-1);
			for(char c : JSON.toCharArray()){
				if(c=='{')
					continue;
				else if(c==',' && charStack.empty())
					continue;
				else if(c=='}')
					JSONElements.add(getAll(charStack));
				else
					charStack.push(c);
			}
		}
		
		for (String string : JSONElements) {
			System.out.println(string);
			String[] entries = string.split(",");
			String type = entries[0].split(":")[1];
			int quantity = Integer.parseInt(entries[1].split(":")[1]);
			
			if( quantities.containsKey(type))
				quantities.replace(type,quantities.get(type)+quantity);
			else
				quantities.put(type, quantity);
		}
		//System.out.println(JSON);
		for(String key : quantities.keySet()){
			System.out.println(
					key.replaceAll("\"","")
					   .trim()
					  +":"
					  +quantities.get(key));
		}
			
	}

	private static String getAll(Stack<Character> charStack) {
		String s = "";
		Stack<Character> tempstack = new Stack<Character>();
		while(!charStack.empty())
			tempstack.push(charStack.pop());
		while (!tempstack.empty()) {
			s+=tempstack.pop();
		}
		return s;
	}
}
