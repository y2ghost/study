====CSV.g4====
antlr CSV.g4
javac CSV*.java
grun CSV file -tokens data.csv

====JSON.g4====
antlr JSON.g4 
javac JSON*.java
grun JSON json -tokens t.json

====DOT.g4====
antlr DOT.g4 
javac DOT*.java
grun DOT graph -tokens t.dot

====R.g4====
antlr R.g4 
javac R*.java
grun R prog -tokens t.r
