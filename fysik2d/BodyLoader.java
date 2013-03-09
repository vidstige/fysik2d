package fysik2d;

import java.util.StringTokenizer;
import java.io.*;
import java.net.URL;

public class BodyLoader   {
	public static RigidBody load(String filename) throws Exception {
		return load(new FileReader(filename));
	}

	public static RigidBody load(URL url) throws Exception {
		InputStream myStream =
			new DataInputStream(url.openConnection().getInputStream());
		return load(new InputStreamReader(myStream));
		
	}
	
	public static RigidBody load(Reader inreader) throws Exception {
		BufferedReader in = new BufferedReader(inreader);
		
		RigidBody tmp = new RigidBody(Vec2.NULL, 0.0, 0.1);

		String line = in.readLine();
		while (line != null) {
			StringTokenizer st = new StringTokenizer(line);
			
			double x = Double.parseDouble(st.nextToken());
			double y = Double.parseDouble(st.nextToken());
			
			tmp.addPoint(new Vec2(x, y));
						
			line = in.readLine();
		}
		
		return tmp;
	}
	
}
