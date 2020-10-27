/**
 * Brick-Slider
 * @author Itay Bouganim, 305278384
 * @author Sahar Vaya , 205583453
 */
package Logic;

import java.io.InputStream;
/*
 * A class used to call resource files such as images animation and sounds.
 * With the use of the method in this class Resources can be read from inside a JAR file.
 */
final public class ResourceLoader {

	/*
	 * A methods that loads a File as InputStream from a certain path.
	 * @param path - the path of the file inside the resources folder.
	 * @return InputStream that can stream the file to use during runtime.
	 */
	public static InputStream load(String path)
	{
		InputStream input = ResourceLoader.class.getResourceAsStream(path);
		if(input == null)
			input = ResourceLoader.class.getResourceAsStream("/"+path);
		
		return input;		
	}
}
