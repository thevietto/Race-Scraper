1) Download phantomJS 2.1.1 from http://phantomjs.org/download.html

2) Extract downloaded package and add the path/to/the/phantomjs/bin to the OS PATH variable

3) Check if phantomjs installed correctly by typing "phantomjs --version"

4) Run the RaceScraper program with the folowwing arguments:
race_ids: comma-separated race ids
out_file: output file name, defaults to sportsscraperoutput.csv


For example:
java -jar RaceScraper.jar race_ids=30947,30583 out_file=result.csv
