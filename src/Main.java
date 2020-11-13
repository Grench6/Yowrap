import java.io.File;

public class Main
{
	private static boolean generateMetadata = false;
	private static boolean copyImages = false;
	private static File file_csv;
	private static File folder_read;
	private static File folder_darknet;

	public static void main(String[] args)
	{
		if (args.length >= 1)
		{
			file_csv = new File(args[0]);
			folder_read = file_csv.getParentFile();
			if (args.length >= 2)
			{
				folder_darknet = new File(args[1]);
				generateMetadata = true;
				if (args.length >= 3 && args[2].toLowerCase().compareTo("-copy") == 0)
					copyImages = true;
			}
		} else
		{
			printHelp();
			return;
		}
		if (!file_csv.exists())
		{
			System.out.println("The .csv file \"" + args[0] + "\" could not be found!");
			return;
		}

		CSV csv = CSV.readCSVFromFile(file_csv);
		Wrapper wrapper;
		if (generateMetadata)
			wrapper = new Wrapper(folder_read, folder_darknet, "metadata", "images", copyImages);
		else
			wrapper = new Wrapper(folder_read, folder_darknet, null, null, copyImages);
		wrapper.wrap(csv);
	}

	private static void printHelp()
	{
		System.out.println("Usage: java -jar wrapper.jar [.csv FILE] [output DIRECTORY] [-copy]");
		System.out.println("The images MUST be located in the same directory as the .csv file.");
		System.out.println("The output directory (darknet directory) is optional: If it IS specified, metadata");
		System.out
				.println("files will be genereated; If it IS NOT specified, no metadata files will be generated, and");
		System.out.println(
				"all yolo-coordinates files will be written to the directory from where the images where read.");
		System.out.println("By default, the program does not copy the images, since it can take many time (in large");
		System.out.println(
				"datasets), so the user has to manually copy them later. However, if you would like the program");
		System.out
				.println("to copy the images for you, you can specify the \"-copy\" option at the end of the command.");
		System.out.println();
	}
}
