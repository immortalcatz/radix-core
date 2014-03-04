#!/usr/bin/env python

#-------------------------------------------------------------------------------
# Assemble.py
# Prepares RadixCore to be distributed by packaging the mod
# archive and source archives.
#
# This file, both in source code form or as a compiled binary, is free and
# released into the public domain.
#-------------------------------------------------------------------------------

import os
import sys
import shutil
import zipfile
import subprocess
import traceback
import time
import urllib

modVersion = ""
dirCWD     = os.getcwd() + "/"

def main():
    print "---------------------------------------------"
    print "RadixCore Packaging Script"
    print "---------------------------------------------"

    #Get release version
    global modVersion
    modVersion = raw_input("Enter RadixCore version: ")

    #Get minecraft version
    global minecraftVersion
    minecraftVersion = raw_input("Enter Minecraft version: ");

    global dirBuild
    dirBuild = dirCWD + "Build/" + minecraftVersion + "/"

    if "1.7" in minecraftVersion:
        build()

    log("Finished.")
    openBuildFolder()

def log(message):
    print "> " + message

def logSub(message):
    print "   > " + message

def build():
    global dirGradleBase
    dirGradleBase = dirCWD + "Minecraft/" + minecraftVersion

    choice = raw_input("Run 'gradlew.bat build'? [Y/N]: ")
    print ""

    if choice == "Y" or choice == "y":
        callGradleSubprocess()
    else:
        log("Gradle build SKIPPED.")

    moveBuildToBuildFolder()
    cleanBuild()
    createSourceArchive()

def callGradleSubprocess():
    log("Building RadixCore...")

    os.chdir(dirGradleBase)
    print "--------------------- GRADLE BEGIN ---------------------"
    subprocess.call("gradlew.bat build")
    print "---------------------- GRADLE END ----------------------"

def moveBuildToBuildFolder():
    log("Moving build to build folder...")

    fileFinishedBuild = dirGradleBase + "/build/libs/RadixCore-RadixAssembled.jar"

    if os.path.exists(dirBuild):
        logSub("Removing existing build folder...")
        shutil.rmtree(dirBuild)
        logSub("Waiting to continue...")
        time.sleep(1)

    os.mkdir(dirBuild)
    shutil.copy(fileFinishedBuild, dirBuild + "/_in_RadixCore-" + modVersion + " MC-" + minecraftVersion + ".zip")

def cleanBuild():
    log("Cleaning RadixCore...")

    fileInArchive = dirBuild + "/_in_RadixCore-" + modVersion + " MC-" + minecraftVersion + ".zip"
    fileOutArchive = dirBuild + "/_out_RadixCore-" + modVersion + " MC-" + minecraftVersion + ".zip"

    zipInArchive = zipfile.ZipFile(fileInArchive, "r", zipfile.ZIP_DEFLATED)
    zipOutArchive = zipfile.ZipFile(fileOutArchive, "w", zipfile.ZIP_DEFLATED)

    for file in zipInArchive.filelist:
        if file.filename.startswith("META-INF/"):
            logSub("Skipping: " + file.filename)
        else:
            zipOutArchive.writestr(file.filename, zipInArchive.read(file))

    zipInArchive.close()
    zipOutArchive.close()

    os.remove(fileInArchive)
    os.rename(fileOutArchive, str(fileOutArchive).replace("_out_", ""))

def openBuildFolder():
    explorerFriendlyName = str(dirBuild).replace("/", "\\")
    subprocess.Popen(r'explorer /root,"' + explorerFriendlyName + '"')

def createSourceArchive():
    linesOfCode = 0

    log("Building source archive...")

    sourceFolder = dirGradleBase + "/src/main/java/com/"
    sourceArchive = zipfile.ZipFile(dirBuild + "/RadixCore-" + modVersion + " MC-" + minecraftVersion + " - Source.zip", "w", zipfile.ZIP_DEFLATED)
    sourceFiles = os.listdir(sourceFolder)

    for root, dirs, files in os.walk(sourceFolder):
        for fileName in files:
            containsCorrectHeader = False
            fullPath = os.path.join(root, fileName)
            archiveName = fullPath.replace(sourceFolder, "com/")
            sourceArchive.write(fullPath, archiveName)

            with open(fullPath) as f:
                lines = f.readlines()

                for line in lines:
                    linesOfCode += 1

                    if fileName in line:
                        containsCorrectHeader = True

            if not containsCorrectHeader:
                logSub("WARNING: Malformed header on " + fileName + ".")

    logSub(str(linesOfCode) + " lines of code.")
    print ""
    sourceArchive.close()

if __name__ == "__main__":
    try:
        os.system("color 0F")
        main()
    except Exception as e:
        print "--------------Unexpected exception--------------"
        print e
        traceback.print_exc()

    raw_input("Press any key to exit...")
