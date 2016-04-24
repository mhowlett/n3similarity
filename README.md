## /now person similarity

This is a proof of concept of a methodology for measuring the similarity
of people on the nownownow.com website.

More information: https://www.matthowlett.com/n3simiarity.html


### Getting it running:

No tagger models have been included in this repository. To get a suitable model in the location
expected by n3similarity, download the Stanford POS tagger distribution [english only, version
3.6] to /data/taggers and unzip it. This is available from:

http://nlp.stanford.edu/software/tagger.shtml#Download

To build:

    mvn package

To run:

    cd target
    java -cp n3similarity-1.0-SNAPSHOT-jar-with-dependencies.jar com.mhowlett.Main

You can also open n3similarity in IntelliJ Idea and build/run it from there.


### License

GPL. Note that the Stanford POS tagger is also released under the GPL.