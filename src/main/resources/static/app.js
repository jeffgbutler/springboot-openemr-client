new Vue({
    el: "#app",
    data: {
        loggedIn: false,
        userName: '',
        patientData: ''
    },
    mounted:function() {
        fetch('/user', {method: 'GET'})
            .then((res) => {
                if (!res.ok) {
                    throw new Error(`Received response ${res.status}`);
                }
                return res.json();
            })
            .then((data) => {
                this.loggedIn = true;
                this.userName = data.name;
            })
            .catch((err) => {
                this.loggedIn = false;
            });
    },
    methods: {
        logout: function() {
            fetch('/logout', {method: 'POST'})
            .then((res) => {
                if (!res.ok) {
                    throw new Error(`Received logout response ${res.status}`);
                }
            });
            this.loggedIn = false;
            this.userName = '';
        },
        getPatients() {
            fetch('/patient', {method: 'GET'})
                .then((res) => {
                    if (!res.ok) {
                        throw new Error(`Received patient response ${res.status}`);
                    }
                    return res.json();
                })
                .then((data) => {
                    this.patientData = JSON.stringify(data);
                })
                .catch((err) => {
                    this.patientData = err;
                });
        }
    }
});
