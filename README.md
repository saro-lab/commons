### saro commons
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/me.saro/commons/badge.svg)](https://maven-badges.herokuapp.com/maven-central/me.saro/commons)
[![GitHub license](https://img.shields.io/github/license/saro-lab/commons.svg)](https://github.com/saro-lab/commons/blob/master/LICENSE)


# QUICK START

## maven

``` xml
<dependency>
  <groupId>me.saro</groupId>
  <artifactId>commons</artifactId>
  <version>3.0.0</version>
</dependency>
```

## gradle

```
compile 'me.saro:commons:3.0.0'
```

## repository
- https://search.maven.org/artifact/me.saro/commons
- http://central.maven.org/maven2/me/saro/commons/
- https://mvnrepository.com/artifact/me.saro/commons

## see
- [가리사니 개발자공간](https://gs.saro.me)







# STATIC


## Converter

#### me.saro.commons.Converter

```
Converter.asList(T[]) : List<T>
Converter.splitByToken(String, String) : List<String>
Converter.splitCsvLine(String) : String[]
Converter.toByteArrayOutputStream(InputStream, int) : ByteArrayOutputStream
Converter.toBytes(InputStream, int) : byte[]
Converter.toClassByJson(String, TypeReference<T>) : T
Converter.toHash(HashAlgorithm, String) : byte[]
Converter.toHash(HashAlgorithm, String, String) : byte[]
Converter.toHash(HashAlgorithm, byte[]) : byte[]
Converter.toHashHex(HashAlgorithm, String) : String
Converter.toHashHex(HashAlgorithm, String, String) : String
Converter.toJson(Object) : String
Converter.toList(Enumeration<T>) : List<T>
Converter.toList(Iterable<T>) : List<T>
Converter.toMap(Object[]) : Map<K, V>
Converter.toMapByClass(Object) : Map<String, T>
Converter.toMapByJsonObject(String) : Map<String, Object>
Converter.toMapListByClassList(Object) : List<Map<String, T>>
Converter.toMapListByJsonArray(String) : List<Map<String, Object>>
Converter.toStream(Enumeration<T>) : Stream<T>
Converter.toStream(Enumeration<T>, boolean) : Stream<T>
Converter.toStream(Iterable<T>) : Stream<T>
Converter.toStream(Iterable<T>, boolean) : Stream<T>
Converter.toStreamByResultSet(ResultSet, ThrowableBiFunction<String[], Object[], R>) : Stream<R>
Converter.toStreamByResultSet(ResultSet, ThrowableFunction<ResultSet, R>) : Stream<R>
Converter.toStreamLineNotCloseByTextInputStream(InputStream, String) : Stream<String>
Converter.toString(Exception) : String
Converter.toString(File, String) : String
Converter.toString(InputStream, String) : String
Converter.toString(URL, String) : String
Converter.toStringNotClose(InputStream, String) : String
```


## Bytes

#### me.saro.commons.bytes.Bytes

```
Bytes.decodeBase64(String) : byte[]
Bytes.decodeBase64(String, String) : String
Bytes.encodeBase64String(String, String) : String
Bytes.encodeBase64String(byte[]) : String
Bytes.toBytes(double) : byte[]
Bytes.toBytes(float) : byte[]
Bytes.toBytes(int) : byte[]
Bytes.toBytes(long) : byte[]
Bytes.toBytes(short) : byte[]
Bytes.toBytesByHex(String) : byte[]
Bytes.toDouble(byte[]) : double
Bytes.toDouble(byte[], int) : double
Bytes.toFloat(byte[]) : float
Bytes.toFloat(byte[], int) : float
Bytes.toHex(byte[]) : String
Bytes.toInt(byte[]) : int
Bytes.toInt(byte[], int) : int
Bytes.toLong(byte[]) : long
Bytes.toLong(byte[], int) : long
Bytes.toShort(byte[]) : short
Bytes.toShort(byte[], int) : short
```


## Utils

#### me.saro.commons.Utils

```
Utils.blank(String) : boolean
Utils.bvl(String[]) : String
Utils.createRandomBase62String(int, int) : String
Utils.createRandomString(char[], int) : String
Utils.createRandomString(char[], int, int) : String
Utils.evl(String[]) : String
Utils.executeAllThreads(ExecutorService, List<T>, ThrowableFunction<T, R>) : List<R>
Utils.executeAllThreads(int, List<T>, ThrowableFunction<T, R>) : List<R>
Utils.inputStreamLineReader(InputStream, String, ThrowableFunction<Stream<String>, T>) : T
Utils.inputStreamReader(InputStream, StreamReadConsumer) 
Utils.kill(Closeable) 
Utils.kill(Thread) 
Utils.linkStream(InputStream, OutputStream) 
Utils.nvl(T[]) : T
Utils.random(long, long) : long
Utils.timerTask(ThrowableConsumer<TimerTask>) : TimerTask
Utils.timestamp(ThrowableRunnable) : long
Utils.zerofill(String, int) : String
Utils.zerofill(int, int) : String
Utils.zerofill(long, int) : String
```


## Files

#### me.saro.commons.Files

```
Files.attributesFilter(ThrowablePredicate<BasicFileAttributes>) : Predicate<File>
Files.createFile(File, boolean, InputStream) 
Files.createFile(File, boolean, String, String) 
Files.lineReader(File, String, ThrowableFunction<Stream<String>, T>) : T
Files.lineReader(String, String, ThrowableFunction<Stream<String>, T>) : T
Files.listFilesStream(File) : Stream<File>
Files.listFilesStream(String) : Stream<File>
Files.toBasicFileAttributes(File) : BasicFileAttributes
Files.toFileExt(File) : String
Files.toFileExt(String) : String
Files.validFileExt(File, String[]) : boolean
Files.validFileExt(String, String[]) : boolean
```


## Maps

#### me.saro.commons.Maps

```
Maps.clone(Map<K, V>) : Map<K, V>
Maps.filter(Map<K, V>, Predicate<Map$Entry<K, V>>) : Map<K, V>
Maps.pick(Map<K, V>, K[]) : Map<K, V>
Maps.toMap(Object[]) : Map<K, V>
```


## Zips

#### me.saro.commons.Zips

```
Zips.openFromFile(File, ThrowableTriConsumer<String, ZipEntry, InputStream>) 
Zips.openFromWeb(BasicWeb, ThrowableTriConsumer<String, ZipEntry, InputStream>) 
Zips.openStreamNotClose(InputStream, ThrowableTriConsumer<String, ZipEntry, InputStream>) 
```


## Crypt

#### me.saro.commons.crypt.Crypt

```
Crypt.decrypt(String, byte[], byte[]) : Crypt
Crypt.encrypt(String, byte[], byte[]) : Crypt
to(File, File, boolean) 
to(InputStream, OutputStream) 
toBase64(byte[]) : String
toBase64(byte[], int, int) : String
toBase64ByBase64(String) : String
toBase64ByHex(String) : String
toBytes(byte[]) : byte[]
toBytes(byte[], int, int) : byte[]
toBytesByBase64(String) : byte[]
toBytesByHex(String) : byte[]
toHex(byte[]) : String
toHex(byte[], int, int) : String
toHexByBase64(String) : String
toHexByHex(String) : String
```


## ByteData

#### me.saro.commons.bytes.ByteData

```
ByteData.create() : ByteData
ByteData.create(String) : ByteData
ByteData.create(int, String) : ByteData
bind(OutputStream) : ByteData
bind(OutputStream, int) : ByteData
fillSpace() : ByteData
insert(String, int) : ByteData
insert(byte[], int) : ByteData
insert(byte[], int, int, int) : ByteData
insertFill(byte, int, int) : ByteData
insertFillSpace(int, int) : ByteData
insertFixed(String, int, byte, int) : ByteData
insertFixedAlignRight(String, int, byte, int) : ByteData
moveWritePointer(int) : ByteData
newByteData(int, int) : ByteData
read(int) : byte[]
readIgnore(int) : ByteData
readIgnoreCurrentLine() : ByteData
readIgnoreMatch(byte) : ByteData
readText(int, boolean) : String
readTextAlignRight(int, boolean) : String
readTextInt(int, int) : int
readTextLong(int, long) : long
rectifyWritePointer() : ByteData
size() : int
toBytes() : byte[]
toBytes(int, int) : byte[]
toString() : String
write(File) : ByteData
write(File, String) : ByteData
write(InputStream) : ByteData
write(InputStream, int) : ByteData
write(String) : ByteData
write(byte[]) : ByteData
write(byte[], int, int) : ByteData
writeFill(byte, int) : ByteData
writeFillSpace(int) : ByteData
writeFixed(String, int, byte) : ByteData
writeFixed(int, int, byte) : ByteData
writeFixedAlignRight(String, int, byte) : ByteData
writeFixedAlignRight(int, int, byte) : ByteData
writeLine1() : ByteData
writeLine2() : ByteData
```


## Web

#### me.saro.commons.web.Web

```
Web.custom(String, String) : Web
Web.delete(String) : Web
Web.get(String) : Web
Web.patch(String) : Web
Web.post(String) : Web
Web.put(String) : Web
addUrlParameter(String, String) : Web
getRequestCharset() : String
getResponseCharset() : String
readRawResultStream(ThrowableConsumer<InputStream>) : WebResult<String>
saveFile(File, boolean) : WebResult<File>
setConnectTimeout(int) : Web
setContentType(String) : Web
setContentTypeApplicationJson() : Web
setHeader(String, String) : Web
setIgnoreCertificate(boolean) : Web
setReadTimeout(int) : Web
setRequestCharset(String) : Web
setResponseCharset(String) : Web
toCustom(ThrowableFunction<InputStream, R>) : WebResult<R>
toCustom(WebResult<R>, ThrowableFunction<InputStream, R>) : WebResult<R>
toJsonReader() : WebResult<JsonReader>
toJsonTypeReference(TypeReference<T>) : WebResult<T>
toMapByJsonObject() : WebResult<Map<String, Object>>
toMapListByJsonArray() : WebResult<List<Map<String, Object>>>
toPlainText() : WebResult<String>
writeBody(String) : Web
writeBody(byte[]) : Web
writeBodyParameter(String, String) : Web
writeJsonByClass(Object) : Web
```


## Valids

#### me.saro.commons.Valids

```
Valids.isDate(String, String) : boolean
Valids.isMail(String, int) : boolean
Valids.isNotBlank(String[]) : boolean
Valids.isNotNull(Object[]) : boolean
```


## Naming

#### me.saro.commons.Naming

```
Naming.toCamelCase(List<String>) : String
Naming.toDashes(List<String>) : String
Naming.toPascalCase(List<String>) : String
Naming.toUnderscores(List<String>) : String
Naming.toWordsByCamelCase(String) : List<String>
Naming.toWordsByDashes(String) : List<String>
Naming.toWordsByPascalCase(String) : List<String>
Naming.toWordsByUnderscores(String) : List<String>
```

# INSTANCE


## FixedDataFormat

#### me.saro.commons.bytes.FixedDataFormat

```
FixedDataFormat.create(Class<T>) : FixedDataFormat<T>
FixedDataFormat.create(Class<T>, Supplier<T>) : FixedDataFormat<T>
bindBytes(OutputStream, T) 
bindBytes(byte[], int, T) 
toBytes(T) : byte[]
toClass(byte[]) : T
toClass(byte[], int) : T
toClassWithCheckSize(String) : T
toClassWithCheckSize(String, String) : T
toClassWithCheckSize(byte[]) : T
```


## BasicWeb

#### me.saro.commons.web.BasicWeb

```
addUrlParameter(String, String) : Web
getRequestCharset() : String
getResponseCharset() : String
setConnectTimeout(int) : Web
setHeader(String, String) : Web
setIgnoreCertificate(boolean) : Web
setReadTimeout(int) : Web
setRequestCharset(String) : Web
setResponseCharset(String) : Web
toCustom(WebResult<R>, ThrowableFunction<InputStream, R>) : WebResult<R>
writeBody(byte[]) : Web
writeBodyParameter(String, String) : Web
```


## FTP

#### me.saro.commons.ftp.FTP

```
FTP.openFTP(InetAddress, int, String, String) : FTP
FTP.openFTP(String, int, String, String) : FTP
FTP.openFTPS(InetAddress, int, String, String) : FTP
FTP.openFTPS(String, int, String, String) : FTP
FTP.openSFTP(String, int, String, String) : FTP
cd(String) : boolean
close() 
delete(String) : boolean
hasDirectory(String) : boolean
hasFile(String) : boolean
listDirectories() : List<String>
listDirectories(Predicate<String>) : List<String>
listFiles() : List<String>
listFiles(Predicate<String>) : List<String>
mkdir(String) : boolean
path() : String
path(String) : boolean
pwd() : String
recv(List<String>, File) 
recv(String, File) : boolean
send(File) : boolean
send(String, File) : boolean
```


## SSHShell

#### me.saro.commons.ssh.SSHShell

```
SSHShell.open(String, int, String, String, String, ThrowableConsumer<String>) : SSHShell
close() 
cmd(String[]) 
cmdExitAndJoinEOF() 
isConnected() : boolean
joinEOF() 
```


## SSHExecutor

#### me.saro.commons.ssh.SSHExecutor

```
SSHExecutor.just(String, int, String, String, String, String[]) : String
SSHExecutor.open(String, int, String, String, String) : SSHExecutor
close() 
cmd(String[]) : String
```


## DateFormat

#### me.saro.commons.DateFormat

```
DateFormat.format(String, String, String) : String
DateFormat.now() : DateFormat
DateFormat.parse(Date) : DateFormat
DateFormat.parse(String, String) : DateFormat
DateFormat.parse(long) : DateFormat
DateFormat.valid(String, String) : boolean
addDates(int) : DateFormat
addHours(int) : DateFormat
addMilliseconds(int) : DateFormat
addMinutes(int) : DateFormat
addMonth(int) : DateFormat
addYear(int) : DateFormat
clone() : DateFormat
clone() : Object
equals(Object) : boolean
format(String) : String
getDate() : int
getDayOfWeek() : int
getHours() : int
getMilliseconds() : int
getMinute() : int
getMonth() : int
getSeconds() : int
getTimeInMillis() : long
getWeekOfMonth() : int
getWeekOfYear() : int
getYear() : int
setDate(int) : DateFormat
setHours(int) : DateFormat
setMilliseconds(int) : DateFormat
setMinutes(int) : DateFormat
setMonth(int) : DateFormat
setSeconds(int) : DateFormat
setTimeInMillis(long) : DateFormat
setYear(int) : DateFormat
toDate() : Date
toISO8601() : String
toString() : String
toString(String) : String
```


## Excel

#### me.saro.commons.excel.Excel

```
Excel.create() : Excel
Excel.createBulkExcel() : Excel
Excel.open(File) : Excel
autoSizeColumn() : Excel
autoSizeColumn(int) : Excel
getCell(String) : ExcelCell
getCell(int, int) : ExcelCell
getPoiSheet() : Sheet
getRow() : ExcelRow
getRow(int) : ExcelRow
isBulk() : boolean
moveNextRow() : Excel
moveSheet(int) : Excel
output(OutputStream) : Excel
readPivotTable(String, int, ThrowableFunction<List<ExcelCell>, R>) : List<R>
readPivotTable(String, int, int, ThrowableFunction<List<ExcelCell>, R>) : List<R>
readTable(String, int, ThrowableFunction<List<ExcelCell>, R>) : List<R>
readTable(String, int, int, ThrowableFunction<List<ExcelCell>, R>) : List<R>
save(File, boolean) : Excel
writeHorizontalList(String, Collection<Object>) : Excel
writePivotTable(String, Collection<String>, List<T>) : Excel
writeTable(String, Collection<String>, List<T>) : Excel
writeVerticalList(String, Collection<Object>) : Excel
```


## ExcelRow

#### me.saro.commons.excel.ExcelRow

```
ExcelRow.toColumnNameByRowIndex(int) : String
ExcelRow.toRowIndex(String) : int
getCell() : ExcelCell
getCell(int) : ExcelCell
getNextRow() : ExcelRow
getPoiRow(boolean) : Row
isEmpty() : boolean
moveNextCell() : ExcelRow
```


## ExcelCell

#### me.saro.commons.excel.ExcelCell

```
ExcelCell.toCellIndex(String) : int
ExcelCell.toColumnName(int, int) : String
ExcelCell.toColumnNameByCellIndex(int) : String
ExcelCell.toDate(Cell, Date) : Date
ExcelCell.toDouble(Cell, double) : double
ExcelCell.toRowCellIndex(String) : int[]
ExcelCell.toString(Cell, String) : String
getDateValue(Date) : Date
getDoubleValue(double) : double
getFloatValue(float) : float
getIntValue(int) : int
getIntegerStringValue(long) : String
getLongValue(long) : long
getNextCell() : ExcelCell
getNextRowCell() : ExcelCell
getPoiCell(boolean) : Cell
getStringValue() : String
getStringValue(String) : String
isEmpty() : boolean
set(Object) : ExcelCell
```


## JsonReader

#### me.saro.commons.json.JsonReader

```
equals(Object) : boolean
get(String) : Object
get(int) : JsonReader
getInt(String, int) : int
getString(String) : String
into(String) : JsonReader
isArray() : boolean
isObject() : boolean
length() : int
toList() : List<JsonReader>
toString() : String
```


## NullOutputStream

#### me.saro.commons.NullOutputStream

```
write(int) 
```

# FUNCTION INTERFACE
#### me.saro.commons.function.*
```
ThrowableRunnable
ThrowableSupplier<R>
ThrowablePredicate<T>
ThrowableConsumer<T>
ThrowableBiConsumer<T, U>
ThrowableTriConsumer<T, U, V>
ThrowableFunction<T, R>
ThrowableBiFunction<T, U, R>
ThrowableTriFunction<T, U, V, R>
```
