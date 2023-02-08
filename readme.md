# OoliteAddonScanner

This project downloads the Oolite.org main game including all expansions,
scans them for ships and equipment and builds easy to surf HTML files.

It also helps OXP authors of validating their plist files.

## Compile:
Based on Java 11+. Just run the maven build.

## Run the scanner:
Use the scripts `run-scanner.cmd` on Windows and `run-scanner.sh` on Linux.
Or run `mvn run`, or `java -jar ooliteaddonscanner.jar`

## Run the Plist Tester:
Use the scripts `run-plist-tester.cmd` on Windows and `run-plist-tester.sh` on Linux.
Or run

    java -cp ooliteaddonscanner.jar com.chaudhuri.plistcheck.PlistTest

In the UI either type the path to your file, or use the ellipsis button to
browse for one. The Plist Tester will validate the files and display errors.
Fire up your preferred editor, change the file and save it. Immediately the 
Plist Tester will validate again, showing you the impact of your change.