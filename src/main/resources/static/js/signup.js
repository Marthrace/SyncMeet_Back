// Handle Signup Form
document.getElementById('signupForm')?.addEventListener('submit', function(e) {
    e.preventDefault();
    const username = document.getElementById('username').value;
    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;
    
    // In a real app, you would send this to your backend
    console.log('Signup Data:', { username, email, password });
    alert('Account created successfully! Redirecting to login...');
    window.location.href = 'login.html';
});

// Handle Login Form
document.getElementById('loginForm')?.addEventListener('submit', function(e) {
    e.preventDefault();
    const email = document.getElementById('loginEmail').value;
    const password = document.getElementById('loginPassword').value;
    
    // In a real app, you would verify credentials with backend
    console.log('Login Data:', { email, password });
    alert('Login successful! Redirecting to dashboard...');
    // window.location.href = 'dashboard.html';
});