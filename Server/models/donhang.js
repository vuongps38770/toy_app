const mongoose = require('mongoose')
const Schema = mongoose.Schema;

const donhang=new Schema({
    userID:{
        type:Schema.Types.ObjectId,
        required:true,
        ref:'users'
    },
    
},{
    timestamps:true,
    collection:'ctdh'
})