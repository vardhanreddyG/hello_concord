var kv = tasks.get("kv");
var dateTime = tasks.get("datetime");

var currentDate = dateTime.current("yyy/MM/dd");

var storedDate = kv.getString(execution, "date");
var storedCount = kv.getLong(execution, "count");

print(
  "currentDate::" + currentDate,
  ",storedDate::" + storedDate,
  ",storedCount::" + storedCount
);

// check the day key exists

// function getDateString() {
//   return new Date().toISOString().split("T")[0];
// }

// var storedDate = kv.getString(execution, "date");
// var storedCount = kv.getLong(execution, "count");

// var today = getDateString();

// if (!storedDate) {
//   print("no date found may this is first run setting date" + getDateString());
//   kv.putString(execution, "date", today);
//   kv.putLong(execution, "count", 0);
// }

// if (storedDate && storedDate === today) {
//   print("number of processes ran today" + storedCount);
//   if (storedCount + 1 > 2) {
//     print("limit reached");
//   } else {
//     kv.putLong(execution, "count", storedCount + 1);
//   }
// }

// if (storedDate && storedDate !== today) {
//   print("This is new day");
//   kv.putString(execution, "date", today);
//   kv.putLong(execution, "count", 0);
// }

// print("date is" + kv.getString(execution, "date"));
// print("count", kv.getLong(execution, "count"));
