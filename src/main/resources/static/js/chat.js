"use strict";
var http = () => axios;
var date = () => moment;
Vue.component("Chat", {
	data: function() {
		return {
			me: user,
			friend: friend,
			http: http,
			moment: date,
			messages: [],
			backward: -1,
			forward: -1,
			typed: '',
			pulling: false
		};
	},
	created: function() {
		var self = this;
		this.intervalid = setInterval(function() {
			console.log('Pulling normally...');
			self.normalPull();
		}, 500);
	},
	methods: {
		pullBackward(event) {
			var chatstate = {
				friend: this.friend.username,
				backward: this.backward,
				forward: this.forward,
				expandingBackward: true
			};
			this.http().post('/messages',chatstate).then(response => {
				this.backward = response.data.backward;
				this.forward = response.data.forward;
				for (var i = response.data.backwardMessages.length-1; i >= 0; i--) {
					this.messages.unshift(response.data.backwardMessages[i]);
				}
				for (var i = 0; i < response.data.forwardMessages.length-1; i++) {
					this.messages.push(response.data.forwardMessages[i]);
				}
			}, err => {
				console.log('Error getting messages.');
			});
		},
		post(event) {
			if (this.typed.length == 0) return;
			var message = {
				body: this.typed,
				sender: this.me.username
			};
			var chatstate = {
					friend: this.friend.username,
					backward: this.backward,
					forward: this.forward,
					forwardMessages: [],
					expandingBackward: false
			};
			chatstate.forwardMessages.push(message);
			this.typed = '';
			this.http().post('/message',chatstate).then(response => {
				console.log('Posted')
			}, err => {
				console.log('Error posting message.');
			});
		},
		normalPull() {
			var chatstate = {
				friend: this.friend.username,
				backward: this.backward,
				forward: this.forward,
				expandingBackward: false
			};
			this.http().post('/messages',chatstate).then(response => {
				this.forward = response.data.forward;
				for (var i = 0; i < response.data.forwardMessages.length; i++) {
					this.messages.push(response.data.forwardMessages[i]);
				}
			}, err => {
				console.log('Error getting messages.');
			});
		},
		formatDate(date) {
			return this.moment()(date).format('MMMM Do YYYY, h:mm:ss a');
		}
	},
	template: `
		<div class="chat">
			<button v-on:click="pullBackward">Load More</button>
			<ul v-if="messages.length > 0">
				<li v-for="message in messages">
					{{message.sender}}: {{message.body}} <em>Sent {{formatDate(message.date)}}</em>
				</li>
			</ul>
			<p v-if="messages.length == 0">No messages :(</p>
			<form action="javascript:void(0)">
				<input type="text" placeholder="Type here..." v-model="typed"/>
				<button v-on:click="post">Send</button>
			</form>
		</div>
	`
});

const app = new Vue({
	el: "#chat",
	template: "<Chat/>"
});