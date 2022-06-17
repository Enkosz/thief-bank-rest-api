function validate(target, rule) {
  const value = target.val();

  if (value && rule(value)) {
    target.addClass("is-valid");
    target.removeClass("is-invalid");
  } else {
    target.addClass("is-invalid");
    target.removeClass("is-valid");
  }
}

let amountModifier = -1;
function formatTransaction(transaction, accountId) {
  let result = {};
  if(transaction.type === "INTERNAL") {
    result.sender = "";
    result.receiver = "";
    result.amount = formatAmount(transaction.amount);
  } else if(transaction.fromAccountId == transaction.toAccountId) { // questo è il caso di transfer verso se stessi
    result.sender = transaction.fromAccountId;
    result.receiver = transaction.fromAccountId;
    result.amount = formatAmount(transaction.amount * amountModifier);
    amountModifier *= -1; 
  } else if (transaction.fromAccountId == accountId && transaction.toAccountId != accountId) {
    result.sender = "";
    result.receiver = transaction.toAccountId;
    result.amount = formatAmount(transaction.amount);
  } else {
    result.sender = transaction.fromAccountId;
    result.receiver = "";
    result.amount = formatAmount(transaction.amount * -1);
  }
  return result;
}

function formatAmount(amount) {
  if (amount == 0)
    return "0";
  if (amount > 0)
    return "+" + amount;
  else
    return "" + amount;
}