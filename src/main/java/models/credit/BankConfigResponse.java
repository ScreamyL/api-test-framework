package models.credit;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class BankConfigResponse {
    @JsonProperty("internalBankSettings")
    private InternalBankSettings internalBankSettings;

    @JsonProperty("creditTypes")
    private List<CreditType> creditTypes;

    public static class InternalBankSettings {
        @JsonProperty("bankName")
        private String bankName;

        @JsonProperty("baseCurrency")
        private String baseCurrency;

        @JsonProperty("apiVersion")
        private String apiVersion;

        // Геттеры и сеттеры
        public String getBankName() { return bankName; }
        public void setBankName(String bankName) { this.bankName = bankName; }

        public String getBaseCurrency() { return baseCurrency; }
        public void setBaseCurrency(String baseCurrency) { this.baseCurrency = baseCurrency; }

        public String getApiVersion() { return apiVersion; }
        public void setApiVersion(String apiVersion) { this.apiVersion = apiVersion; }
    }

    // Геттеры и сеттеры
    public InternalBankSettings getInternalBankSettings() { return internalBankSettings; }
    public void setInternalBankSettings(InternalBankSettings internalBankSettings) {
        this.internalBankSettings = internalBankSettings;
    }

    public List<CreditType> getCreditTypes() { return creditTypes; }
    public void setCreditTypes(List<CreditType> creditTypes) {
        this.creditTypes = creditTypes;
    }
}