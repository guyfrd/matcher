# matcher

Matcher is a Java application for searching multiple key strings in a file.

### URL
The appliction except URL for text file. 
set the URL in the matcher.config file(src/main/matcher.config).

### Matchers
Matchers are threads that wait the be dispatched with a slice of the file. Their target is to find all matches in the slice. 
for setting the number of lines in every slice  - set the filed MAX_LINE_PER_MATCHER in matcher.config.
for setting the number of matchers - set the filed MAX_MATCHERS in matcher.config.

### Search Keys
The keys are set in a text file in src/main/java/com/matcher/keys and include the 50 most common English first names.

### Output
After all matchers finished, the appliction will print all results in the following structure: 
KEY -> [lineOffset: XXX charOffset: XXXX], [lineOffset: XXX charOffset: XXXX]


