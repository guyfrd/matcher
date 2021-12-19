# matcher

Matcher is a Java application for searching multiple key strings in a file.

### Data source
The application accept file text form both URL or file in the the file-system.

### Data source configuration
Set the matcher.config(src/main/matcher.config) file according your input type.<br />
INPUT_SOURCE - URL/FS - for read text from URL or from file-system.<br />
INPUT_PATH - the URL or path to your file according the INPUT_SOURCE. <br />
the default path apply to file in src/main/java/com/matcher/bigFile.<br />

### Matchers
Matchers are threads that wait to dispatched with a slice of the file.<br />
Their target is to find appearance of the search keys in the slice.<br />
for setting the number of lines in every slice  - set the filed MAX_LINE_PER_MATCHER in matcher.config.<br />
for setting the number of matchers - set the filed MAX_MATCHERS in matcher.config.<br />

### Search Keys
The keys are set in a text file in src/main/java/com/matcher/keys and include the 50 most common English first names.

### Case-sensistiv 
By default, matcher is case-sensitive, for ignoring the case, set the filed CASE_SENSITIVE to *false*. 
### aggregator
aggregate all the matcher results, map them by the search-key and print the results.

### Output
After all matchers finished, the appliction will print all results in the following structure: 
KEY -> [lineOffset: XXX charOffset: XXXX], [lineOffset: XXX charOffset: XXXX]


