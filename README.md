# Yowrap

So, you want to be able to generate files for trainning Yolo with any tagging tool? That is exactly what this is for.

A simple Java utility wrapper. You can run this tool from command line to convert the CSV files, generated by your favorite tagging tool, into those files supported by Darknet in the trainning of Yolo models.

I highly recommend using **[VoTT](https://github.com/Microsoft/VoTT)** since it is fast and stable, but if you hate Microsoft you can use any other... as long as they generate a csv file like the one shown bellow:

*export.csv*
```
"image","xmin","ymin","xmax","ymax","label"
"test_00000000.jpg",581.7152961980548,325.0117924528302,690.3625110521663,618.2193396226415,"apple"
"test_00000001.jpg",553.42175066313,305.76650943396226,710.7338638373121,626.1438679245283,"pen"
"test_00000002.jpg",553.42135466316,308.76650943396231,710.7338633463474,566.1438679245124,"apple-pen"
[...]
```

If your tagging tool does not generate the *exact* same files as the above, do not worry, you can easily modify the source code to adjust the differences.

If you are using VoTT there is almost not need for you to modify the source, just download the lastest release and you are good to go.
## Usage

`java -jar wrapper.jar [.csv FILE] [output DIRECTORY] [options]`

**File and directory**

The images MUST be located in the same directory as the .csv file.
The output directory (darknet directory) is optional: If it IS specified, metadata
files will be genereated; If it IS NOT specified, no metadata files will be generated, and
all yolo-coordinates files will be written to the directory from where the images where read.

**Options**

*-copy* : By default the program does not copy the images, since it can take many time (in large
datasets), so the user has to manually copy them later. However, if you would like the program
to copy the images for you, you can specify this option.

*-style_verify* : By default the program generates a structure of folders and subfolders that is
exactly the same as the darknet training example. You should leave it that way, but if you want to
verify that this program is working correctly, you can specify this option, and then you can verify
everything with yolo-mark.

*-rename* : By default the program does not rename the generated yolo-files nor the copied images
(in case the copy option is specified). However, there can be times (whlie working with videos) when
it is better to have your files renamed in a linear sequence (0.jpg, 0.txt, 1.jpg, 1.txt ...) in
order to prevent weird names that darknet may not proccess correcly, or just to have some order.

**Example**

`java -jar wrapper.jar C:\Users\Grench\Desktop\My_Training_Project\vott-csv-export\export.csv C:\Users\Grench\Desktop\out -copy -rename`
