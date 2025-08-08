// Authentication Controller
const authController = {
    init() {
        document.getElementById('loginForm').addEventListener('submit', this.handleLogin);
    },

    handleLogin(e) {
        e.preventDefault();
        
        const email = document.getElementById('email').value;
        const password = document.getElementById('password').value;
        const errorDiv = document.getElementById('loginError');

        // Basic validation
        if (!validateEmail(email)) {
            this.showError('Please enter a valid email address');
            return;
        }

        if (password.length < 6) {
            this.showError('Password must be at least 6 characters');
            return;
        }

        // Submit login
        fetch('/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: `email=${encodeURIComponent(email)}&password=${encodeURIComponent(password)}`
        })
        .then(response => {
            if (response.redirected) {
                // Login successful
                currentUser = { email: email };
                showMainContent();
                loadDashboard();
                errorDiv.style.display = 'none';
            } else {
                return response.text();
            }
        })
        .then(text => {
            if (text && text.includes('Invalid email or password')) {
                this.showError('Invalid email or password');
            }
        })
        .catch(error => {
            console.error('Login error:', error);
            this.showError('Login failed. Please try again.');
        });
    },

    showError(message) {
        const errorDiv = document.getElementById('loginError');
        errorDiv.textContent = message;
        errorDiv.style.display = 'block';
    }
};

// Initialize when DOM is loaded
document.addEventListener('DOMContentLoaded', () => {
    authController.init();
});