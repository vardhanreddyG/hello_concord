/** Determines if there's enough information to move to processing */
function validateFormArgs() {
  var result = false;
  print("Product Name: " + productInfo.ProductName);
  print("AwEnvironment: " + productInfo.AwEnvironment);
  print("DeleteFilesOrActions: " + productInfo.DeleteFilesOrActions);
  // if (/[^a-zA-Z0-9\-\/ \.\_]/.test(productInfo.ProductName)) {
  // // invalid characters detected
  // var badCharIndex = productInfo.ProductName.search(/[^a-zA-Z0-9\-\/ \.\_]/);
  // print("Invalid characters in Product Name: " + productInfo.ProductName);
  // print(" " + repeatStr(" ", badCharIndex) + "^");
  // execution.setVariable("execError", "Invalid characters in Product Name: " + productInfo.ProductName);
  // return false;
  // }
  if (/^cert|prod$/i.test(productInfo.AwEnvironment) === false) {
    print("Invalid AirWatch Environment value: " + productInfo.AwEnvironment);
    return false;
  }
  return true;
}
function repeatStr(str, count) {
  var output = "";
  for (i = 0; i < count; ++i) {
    output += str;
  }
  return output;
}
if (validateFormArgs()) {
  print("Form arguments are valid.");
  execution.setVariable("productName", productInfo.ProductName);
  // execution.setVariable("doDeleteProduct", true);
  execution.setVariable("awEnvironment", productInfo.AwEnvironment);
  execution.setVariable(
    "deleteFilesOrActions",
    productInfo.DeleteFilesOrActions
  );
  print("Product Name: " + productInfo.ProductName);
  print("DeleteFilesOrActions: " + productInfo.DeleteFilesOrActions);
  var nameVersionRegex = /^(.*)\s+(\d+\.\d+\.?\d*\.?\d*\.?\d*)$/g;
  var matches = nameVersionRegex.exec(productInfo.ProductName);
  if (matches !== null) {
    execution.setVariable("appName", matches[1]);
  }
  if (/^prod$/i.test(productInfo.AwEnvironment)) {
    execution.setVariable("apiUrl", apiUrlProd);
    execution.setVariable("apiUser", apiUserProd);
    execution.setVariable("apiPass", apiPassProd);
    execution.setVariable("apiKey", apiKeyProd);
    execution.setVariable("rootGroup", "48773");
  } else {
    execution.setVariable("apiUrl", apiUrlCert);
    execution.setVariable("apiUser", apiUserCert);
    execution.setVariable("apiPass", apiPassCert);
    execution.setVariable("apiKey", apiKeyCert);
    execution.setVariable("rootGroup", "2499");
  }
} else {
  execution.setVariable("doDeleteProduct", false);
}
