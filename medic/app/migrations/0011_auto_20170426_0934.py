# -*- coding: utf-8 -*-
# Generated by Django 1.10.4 on 2017-04-26 09:34
from __future__ import unicode_literals

from django.db import migrations, models
import django.db.models.deletion


class Migration(migrations.Migration):

    dependencies = [
        ('app', '0010_auto_20170426_0903'),
    ]

    operations = [
        migrations.CreateModel(
            name='MyUser',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('name', models.CharField(max_length=128)),
                ('password', models.CharField(max_length=128)),
            ],
        ),
        migrations.AlterField(
            model_name='userfolders',
            name='user',
            field=models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='app.MyUser'),
        ),
        migrations.AlterField(
            model_name='userinfo',
            name='user',
            field=models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='app.MyUser'),
        ),
    ]
