document.addEventListener('DOMContentLoaded', function() {
    // Global variables
    let currentUser = null;
    const API_BASE_URL = '/api';

    // Form validation
    const forms = document.querySelectorAll('.needs-validation');
    forms.forEach(form => {
        form.addEventListener('submit', function(event) {
            if (!form.checkValidity()) {
                event.preventDefault();
                event.stopPropagation();
            }
            form.classList.add('was-validated');
        });
    });

    // Login form handler
    document.getElementById('loginForm').addEventListener('submit', async function(e) {
        e.preventDefault();
        if (this.checkValidity()) {
            try {
                const response = await fetch(`${API_BASE_URL}/auth/login`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({
                        email: document.getElementById('email').value,
                        password: document.getElementById('password').value
                    })
                });

                if (response.ok) {
                    const data = await response.json();
                    currentUser = data;
                    showDashboard();
                    loadAccountInfo();
                } else {
                    alert('Invalid credentials');
                }
            } catch (error) {
                console.error('Login error:', error);
                alert('Login failed');
            }
        }
    });

    // Load account information
    async function loadAccountInfo() {
        try {
            const response = await fetch(`${API_BASE_URL}/accounts/me`, {
                headers: {
                    'Authorization': `Bearer ${currentUser.token}`
                }
            });
            const accountData = await response.json();
            
            document.getElementById('userEmail').textContent = accountData.email;
            document.getElementById('accountType').textContent = accountData.accountType;
            document.getElementById('balance').textContent = accountData.balance.toFixed(2);

            // Show/hide sections based on account type
            const isParent = accountData.accountType === 'PARENT';
            document.getElementById('parentSection').classList.toggle('hidden', !isParent);
            document.getElementById('parentToChildSection').classList.toggle('hidden', !isParent);
            document.getElementById('childToParentSection').classList.toggle('hidden', isParent);

            if (isParent) {
                loadChildAccounts();
            }
        } catch (error) {
            console.error('Error loading account info:', error);
        }
    }

    // Load child accounts for parent
    async function loadChildAccounts() {
        try {
            const response = await fetch(`${API_BASE_URL}/accounts/me/children`, {
                headers: {
                    'Authorization': `Bearer ${currentUser.token}`
                }
            });
            const children = await response.json();
            
            const childList = document.getElementById('childAccountsList');
            const childSelect = document.getElementById('childSelect');
            
            childList.innerHTML = '';
            childSelect.innerHTML = '<option value="">Select a child account</option>';

            children.forEach(child => {
                // Add to the list
                childList.innerHTML += `
                    <div class="list-group-item">
                        <div class="d-flex justify-content-between align-items-center">
                            <div>
                                <h6 class="mb-0">${child.email}</h6>
                                <small>Balance: $${child.balance.toFixed(2)}</small>
                            </div>
                        </div>
                    </div>
                `;

                // Add to the select dropdown
                childSelect.innerHTML += `<option value="${child.id}">${child.email}</option>`;
            });
        } catch (error) {
            console.error('Error loading child accounts:', error);
        }
    }

    // Transaction form handlers
    document.getElementById('depositForm').addEventListener('submit', handleTransaction.bind(null, 'deposit'));
    document.getElementById('withdrawForm').addEventListener('submit', handleTransaction.bind(null, 'withdraw'));
    document.getElementById('parentToChildForm').addEventListener('submit', handleParentToChildTransfer);
    document.getElementById('childToParentForm').addEventListener('submit', handleChildToParentTransfer);

    // Generic transaction handler
    async function handleTransaction(type, e) {
        e.preventDefault();
        if (e.target.checkValidity()) {
            const amount = document.getElementById(`${type}Amount`).value;
            try {
                const response = await fetch(`${API_BASE_URL}/transactions/${type}`, {
                    method: 'POST',
                    headers: {
                        'Authorization': `Bearer ${currentUser.token}`,
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({ amount: parseFloat(amount) })
                });

                if (response.ok) {
                    alert(`${type.charAt(0).toUpperCase() + type.slice(1)} successful`);
                    loadAccountInfo();
                    e.target.reset();
                } else {
                    const error = await response.text();
                    alert(error || `${type} failed`);
                }
            } catch (error) {
                console.error(`${type} error:`, error);
                alert(`${type} failed`);
            }
        }
    }

    // Parent to child transfer handler
    async function handleParentToChildTransfer(e) {
        e.preventDefault();
        if (e.target.checkValidity()) {
            const childId = document.getElementById('childSelect').value;
            const amount = document.getElementById('parentToChildAmount').value;

            try {
                const response = await fetch(`${API_BASE_URL}/transactions/parent-to-child`, {
                    method: 'POST',
                    headers: {
                        'Authorization': `Bearer ${currentUser.token}`,
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({
                        childId: childId,
                        amount: parseFloat(amount)
                    })
                });

                if (response.ok) {
                    alert('Transfer to child successful');
                    loadAccountInfo();
                    e.target.reset();
                } else {
                    const error = await response.text();
                    alert(error || 'Transfer failed');
                }
            } catch (error) {
                console.error('Transfer error:', error);
                alert('Transfer failed');
            }
        }
    }

    // Child to parent transfer handler
    async function handleChildToParentTransfer(e) {
        e.preventDefault();
        if (e.target.checkValidity()) {
            const amount = document.getElementById('childToParentAmount').value;

            try {
                const response = await fetch(`${API_BASE_URL}/transactions/child-to-parent`, {
                    method: 'POST',
                    headers: {
                        'Authorization': `Bearer ${currentUser.token}`,
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({
                        amount: parseFloat(amount)
                    })
                });

                if (response.ok) {
                    alert('Transfer to parent successful');
                    loadAccountInfo();
                    e.target.reset();
                } else {
                    const error = await response.text();
                    alert(error || 'Transfer failed');
                }
            } catch (error) {
                console.error('Transfer error:', error);
                alert('Transfer failed');
            }
        }
    }

    // Helper function to show dashboard
    function showDashboard() {
        document.getElementById('loginSection').classList.add('hidden');
        document.getElementById('dashboardSection').classList.remove('hidden');
    }
});