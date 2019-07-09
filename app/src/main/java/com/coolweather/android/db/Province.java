package  com.coolweather.android.db;

import org.litepal.crud.DataSupport;

    public class Province extends DataSupport {
        private int id;
        private String provinceName;
        private int provinceCode;

        public void setId(int id) {
            this.id = id;
        }

        public void setProvinceCode(int pronvinceCode) {
            this.provinceCode = pronvinceCode;
        }

        public void setProvinceName(String pronvinceName) {
            this.provinceName = pronvinceName;
        }

        public int getId() {
            return id;
        }

        public int getProvinceCode() {
            return provinceCode;
        }

        public String getProvinceName() {
            return provinceName;
        }
    }


