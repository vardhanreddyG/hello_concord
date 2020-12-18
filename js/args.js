var kv = tasks.get('kv');


var date = new Date().toISOString().split("T")[0];
print("date is",date)
// kv.putString("date",date)
var count = kv.getLong(execution,'count') || 0
kv.putLong("count",count);

print("date is",kv.getString(execution,'date'));
print("count",kv.getLong(execution,'count'))