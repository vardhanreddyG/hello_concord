var kv = tasks.get('kv');

print("js script")

var date = new Date().toISOString().split("T")[0];
print("date is",date)
kv.putString(execution,"date",date)
var count = kv.getLong(execution,'count') || 0
kv.putLong(execution,"count",count);

print("date is",kv.getString(execution,'date'));
print("count",kv.getLong(execution,'count'))