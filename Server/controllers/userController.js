const User = require('../models/users');

exports.createUser= async (req,res)=>{
    try {
        const phone= await User.findOne({phonenumber:req.body.phonenumber})
        if(phone)
            return res.status(409).json({message: "Số điện thoại đã tồn tại"})
        const email= await User.findOne({phonenumber:req.body.email})
        if(email)
            return res.status(408).json({message: "Email đã tồn tại"})
        const user = new User(req.body);
        await user.save();
        res.status(201).json(user);   
    } catch (error) {
        res.status(400).json({message:error.message});
    }
}
exports.validetInf = async (req,res)=>{
    try {
        const {phonenumber,email} = req.body
        const find = await User.findOne({phonenumber,email})
        if(find){
            console.log("da ton tai")
            return res.status(409).json({message: " da ton tai"})
        }
        res.status(200).json({message: "ok"})
    } catch (error) {
        res.status(400).json({message:error.message});
    }
}
exports.getUsers = async (req, res) => {
    try {
        const users = await User.find();
        res.json(users);
    } catch (error) {
        res.status(500).json({message:error.message});
    }
};
exports.findAccount  = async (req, res) => {
    const {phonenumber,password} = req.body
    await console.log("thông tin yêu cầu:\n",req.body);
    try {
        const user = await User.findOne({phonenumber,password});
        if(!user)
            return res.status(404).json({ message: 'Tài khoản không tồn tại hoặc mật khẩu không chính xác'});
        console.log("truy vấn thành công")
        res.status(200).json({ 
            userId: user._id,
            username: user.username,
            phonenumber: user.phonenumber,
            email: user.email,
            role: user.role
        });
        
    } catch (error) {
        res.status(500).json({message:error.message});
    }
};
exports.getUserById = async (req, res) => {
    try {
        const user = await User.findById(req.params.id);
        if (!user) return res.status(404).json({ message: 'User not found' });
        res.json(user);
    } catch (error) {
        res.status(500).json({message:error.message});
    }
};

exports.updateUser = async (req, res) => {
    try {
        const user = await User.findByIdAndUpdate(req.body._id, req.body, { new: true });
        if (!user) return res.status(404).json({ message: 'User not found' });
        res.json(user);
    } catch (error) {
        res.status(400).json({message:error.message});
    }
};

exports.deleteUser = async (req, res) => {
    try {
        const user = await User.findByIdAndDelete(req.params.id);
        if (!user) return res.status(404).json({ message: 'User not found' });
        res.json({ message: 'User deleted successfully' });
    } catch (error) {
        res.status(500).json({message:error.message});
    }
};

exports.findAccountByEmail = async (req,res)=>{
    try {
        const user = await User.findOne({email:req.params.email})
        if(user){return res.status(200).json(user)} 
        res.status(404).json({message: "not found"})
    } catch (error) {
        res.status(500).json({message:error.message});
    }
}