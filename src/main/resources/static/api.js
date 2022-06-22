function getAccounts() {
  return request("GET", "account");
}

function getAccountById(accountId) {
  return request("GET", "account/" + accountId);
}

function transfer(fromAccountId, toAccountId, amount) {
  return request("POST", "transfer", { fromAccountId, toAccountId, amount });
}

function getBaseEndpoint() {
  return window.location.origin
}

function request(type, url, data) {
  return new Promise((resolve, reject) => {
    $.ajax({
      type: type,
      url: `${getBaseEndpoint()}/api/${url}`,
      contentType: "application/json",
      data: data ? JSON.stringify(data) : "",
      cache: false,
      success: resolve,
      error: (error) => {
        if (error.readyState === 4) {
          const json = error.responseJSON;

          alert(getErrorMessage(json.code));
          reject(json);
        } else {
          reject(error);
          alert("Impossibile comunicare con il server, riprovare");
        }
      },
    });
  });
}

function getErrorMessage(code) {
  switch (code) {
    case "ACCOUNT_NOT_FOUND":
      return "Errore, impossibile trovare l'account";
    case "TRANSACTION_NOT_FOUND":
      return "Errore, impossibile trovare la transazione";
    case "INVALID_REVERT":
      return "Errore, impossibile eseguire il revert della transazione"
    case "INVALID_TRANSACTION":
      return "Errore, impossibile eseguire la transazione, credito insufficiente";
    default:
      return "Si è verificato un errore, riprovare";
  }
}
