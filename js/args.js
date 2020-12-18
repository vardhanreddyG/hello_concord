var kv = tasks.get('kv');

print("js script")



function getDateString(){
    return new Date().toISOString().split("T")[0]
}

var storedDate = kv.getString(execution,'date');
var storedCount = kv.getLong(execution,'count');

var today = getDateString();


if(!storedDate){
    print("no date found may this is first run setting date" + getDateString());
    kv.putString(execution,'date',today)
    kv.putLong(execution,'count',0)
}



if(storedDate && storedDate === today){
    print("number of processes ran today" + storedCount)
    kv.putLong(execution,'count',storedCount+1)
    if(storedCount > 2){
        print("limit reached")
    }
}

if(storedDate && storedDate !== today ){
    print("This is new day")
    kv.putString(execution,'date',today)
    kv.putLong(execution,'count',0)
}

print('date is' + kv.getString(execution,'date'));
print('count',kv.getLong(execution,'count'))