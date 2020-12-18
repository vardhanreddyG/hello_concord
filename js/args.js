var kv = tasks.get('kv');

print("checking args")

kv.putString("date",new Date().toISOString().split("T")[0])
var count = kv.getInteger('count') || 0
kv.putInteger("count",count);

print("date is",kv.getString('date'));
print("count",kv.getInteger('count'))