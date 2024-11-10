
const mongoose= require('mongoose')
const Schema=mongoose.Schema

//collection user (bangr users)
const Users = new Schema({
    username:{
        type:String, 
        maxLength:255,
        required: true
    },
    password:{
        type:String,
        required: true
    },
    phonenumber:{
        type:String,
        unique:true,
        required: true
    },
    email:{
        type:String,
        unique:true,
        required: true
    },
    role:{
        type:Number,
        required:true
    },
},{
    collection:'users',
    timestamps:true
})

module.exports=mongoose.model('users',Users)