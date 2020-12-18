
def kv = tasks.get('kv')


Date today = new Date()
String dateString = today.format("dd-MM-yyy")
String previousDate = kv.getString('date');
long previousCount = kv.getLong('count');
long count = !previousCount? 0: previousCount+1
String date = !previousDate? dateString: previousDate

println("previous date" + date)
println("prevois count" + count)

if(!previousDate) kv.putString("date",dateString)

if(date === dateString){
    println("today processes");
    kv.putLong("count",count)
}


println(dateString)
println("date is" + kv.getString('date'))
println("count is" + kv.getLong('count'))