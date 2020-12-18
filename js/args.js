var kv = tasks.get('kv');

print("checking args",new Date().toISOString().split("T")[0])

kv.putString("date",new Date().toISOString().split("T")[0])
var count = kv.getLong('count') || 0
kv.putLong("count",count);

print("date is",kv.getString('date'));
print("count",kv.getLong('count'))