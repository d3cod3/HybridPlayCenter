//
//  InstructionSlideViewController.m
//  HybridPlayCenter
//
//  Created by n3m3da on 9/6/15.
//  Copyright (c) 2015 Apportable. All rights reserved.
//

#import "InstructionSlideViewController.h"

@interface InstructionSlideViewController ()

@end

@implementation InstructionSlideViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    self.backgroundImageView.image = [UIImage imageNamed:self.imageFile];
    
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

@end
