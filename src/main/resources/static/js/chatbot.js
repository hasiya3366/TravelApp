document.addEventListener('DOMContentLoaded', () => {
    const widget = document.getElementById('chatbot-widget');
    if (!widget) return;

    const panel = widget.querySelector('.chatbot-panel');
    const launcher = widget.querySelector('.chatbot-launcher');
    const closeButton = widget.querySelector('.chatbot-close');
    const messagesContainer = widget.querySelector('.chatbot-messages');
    const quickLinks = Array.from(widget.querySelectorAll('.chatbot-quick-link'));

    const knowledge = [
        {
            patterns: ['book', 'booking', 'reserve'],
            reply: 'To book a trip, go to the Packages page and choose the package you like, then follow the Book Now button.'
        },
        {
            patterns: ['package', 'packages', 'tours', 'view package'],
            reply: 'We offer beach escapes, cultural tours, wildlife safaris, and tailor-made Sri Lanka trips. Visit the Packages page to explore current offers.'
        },
        {
            patterns: ['place', 'places', 'destination', 'destinations', 'travel place', 'travel places', 'show travel places'],
            reply: 'To see available travel places, go to the Destinations page. You can browse the listed destinations and their details there.'
        },
        {
            patterns: ['contact', 'support', 'help', 'contact us'],
            reply: 'For direct contact, use the Contact page to send a message, or call us immediately on 0701234567.'
        },
        {
            patterns: ['price', 'prices', 'cost', 'price list', 'budget', 'package prices'],
            reply: 'Package prices are listed on the Packages page and package detail pages. For custom travel plans, contact us through the Contact page.'
        },
        {
            patterns: ['login', 'sign in', 'account', 'register', 'registration', 'how to register'],
            reply: 'To register, click the Register link on the Login page, fill in your email and password, and confirm. Once registered, you can sign in and book travel packages.'
        },
    ];

    function appendMessage(text, sender) {
        const item = document.createElement('div');
        item.className = `chatbot-message ${sender}`;
        item.textContent = text;
        messagesContainer.appendChild(item);
        messagesContainer.scrollTop = messagesContainer.scrollHeight;
    }

    function getReply(text) {
        const normalized = text.toLowerCase();
        for (const item of knowledge) {
            if (item.patterns.some(pattern => normalized.includes(pattern))) {
                return item.reply;
            }
        }
        return 'I can help with booking, travel places, package prices, and contact details. Please choose one of the help options above.';
    }

    function openPanel() {
        panel.classList.add('open');
        launcher.setAttribute('aria-expanded', 'true');
        if (!messagesContainer.querySelector('.chatbot-message.bot')) {
            appendMessage('Hello! I can guide you through booking, travel places, prices, and support. Choose one of the help options below.', 'bot');
        }
    }

    function closePanel() {
        panel.classList.remove('open');
        launcher.setAttribute('aria-expanded', 'false');
    }

    launcher.addEventListener('click', openPanel);
    closeButton.addEventListener('click', closePanel);

    quickLinks.forEach(button => {
        button.addEventListener('click', () => {
            const prompt = button.textContent.trim();
            appendMessage(prompt, 'user');
            setTimeout(() => {
                appendMessage(getReply(prompt), 'bot');
            }, 240);
        });
    });
});
