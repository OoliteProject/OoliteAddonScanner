# OoliteAddonScanner

This project downloads the Oolite.org main game including all expansions,
scans them for ships and equipment and builds easy to surf HTML files.

These HTML files are generated regularly with the output being downloadable as Release.

This project also helps OXP authors by validating their plist files, plus it
helps maintaining the catalog of available OXPs based on a simple list of
OXZ download URLs.

## Compile:
Based on Java 11+. Just run the maven build.

## Run the scanner:
Use the scripts `run-scanner.cmd` on Windows and `run-scanner.sh` on Linux.
Or run `mvn run`, or `java -jar ooliteaddonscanner.jar`

The scanner will fire up, download the relevant files and create a file structure
in your current directory.

## Run the Plist Tester:
Use the scripts `run-plist-tester.cmd` on Windows and `run-plist-tester.sh` on Linux.
Or run

    java -cp ooliteaddonscanner.jar com.chaudhuri.plistcheck.PlistTest

In the UI either type the path to your file, or use the ellipsis button to
browse for one. The Plist Tester will validate the files and display errors.
Fire up your preferred editor, change the file and save it. Immediately the 
Plist Tester will validate again, showing you the impact of your change.

## Run the Generator:
Use the scripts `run-generator.cmd` on Windows and `run-generator.sh` on Linux.
Or run

    java -cp target/OoliteAddonScanner-1.0-SNAPSHOT.jar com.chaudhuri.cataloggenerator.Main -in expansionUrls.txt  -out ./catalog.plist

to read the URLs in expansionUrls.txt and write the catalog.plist file.