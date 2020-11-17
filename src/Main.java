import java.io.File;

public class Main
{
	private static boolean renameToLinear = true;
	private static boolean generateMetadata = true;
	private static boolean copyImages = false;
	private static int style = 0;
	private static File file_csv;
	private static File folder_read;
	private static File folder_darknet;
	private static String[] args;

	public static void main(String[] args)
	{
		Main.args = args;
		if (!generateOptions())
			return;
		CSV csv = CSV.readCSVFromFile(file_csv);
		Wrapper wrapper;
		if (generateMetadata)
		{
			if (style == 0)
				wrapper = new Wrapper(folder_read, folder_darknet, "data", "obj", copyImages, renameToLinear);
			else
				wrapper = new Wrapper(folder_read, folder_darknet, "data", "img", copyImages, renameToLinear);
		} else
			wrapper = new Wrapper(folder_read, folder_darknet, null, null, copyImages, renameToLinear);
		wrapper.wrap(csv);
	}

	private static boolean generateOptions()
	{
		if (args.length >= 1)
		{
			file_csv = new File(args[0]);
			folder_read = file_csv.getParentFile();
			if (args.length >= 2)
			{
				folder_darknet = new File(args[1]);
				generateMetadata = true;
			}
		} else
		{
			printHelp();
			return false;
		}
		if (!file_csv.exists())
		{
			System.out.println("The .csv file \"" + args[0] + "\" could not be found!");
			return false;
		}

		if (searchOption("-style_verify"))
			style = 1;
		renameToLinear = searchOption("-rename");
		copyImages = searchOption("-copy");
		return true;
	}

	private static boolean searchOption(String option)
	{
		if (args.length < 3)
			return false;
		for (String current : args)
		{
			if (current.toLowerCase().compareTo(option.toLowerCase()) == 0)
				return true;
		}
		return false;
	}

	private static void printHelp()
	{
		System.out.println("Usage: java -jar wrapper.jar [.csv FILE] [output DIRECTORY] [options]");
		System.out.println();
		System.out.println("----- [.csv FILE] [output DIRECTORY] -----");
		System.out.println("The images MUST be located in the same directory as the .csv file.");
		System.out.println("The output directory (darknet directory) is optional: If it IS specified, metadata");
		System.out.println("files will be genereated; If it IS NOT specified, no metadata files will be generated, and");
		System.out.println("all yolo-coordinates files will be written to the directory from where the images where read.");
		System.out.println();
		System.out.println("----- [options] -----");
		System.out.println("\"-copy\": By default, the program does not copy the images, since it can take many time (in large");
		System.out.println("datasets), so the user has to manually copy them later. However, if you would like the program");
		System.out.println("to copy the images for you, you can specify this option.");
		System.out.println();
		System.out.println("\"-style_verify\": By default the program generates a structure of folders and subfolders that is");
		System.out.println("exactly the same as the darknet_trainning example. You should leave it that way, but if you want to");
		System.out.println("verify that this program is working correctly, you can specify this option, and then you can verify");
		System.out.println("everything with yolo-mark.");
		System.out.println();
		System.out.println("\"-rename\": By default the program does not rename the generated yolo-files nor the copied images");
		System.out.println("(in case the copy option is specified). However, there can be times (whlie working with videos) when");
		System.out.println("it is better to have your files renamed in a linear sequence (0.jpg, 0.txt, 1.jpg, 1.txt ...) in");
		System.out.println("order to prevent weird names that darknet may not proccess correcly, or just to have some order.");
	}
}
